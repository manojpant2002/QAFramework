import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';

// Custom metrics
const successRate = new Rate('success_rate');
const requestDuration = new Trend('request_duration');
const requestsPerSecond = new Counter('requests_per_second');
const timeToFirstByte = new Trend('time_to_first_byte');
const pageLoadTime = new Trend('page_load_time');

// Required environment variables
const BASE_URL = validateUrl(__ENV.BASE_URL);
const VUS = validateNumber(__ENV.VUS, 'VUS', 10000);
const DURATION = validateDuration(__ENV.DURATION, '1s');
const ENDPOINT = __ENV.ENDPOINT || '/api/health';
const COOLDOWN = validateNumber(__ENV.COOLDOWN, 'COOLDOWN', 30); // Cooldown period in seconds

export const options = {
    scenarios: {
        burst: {
            executor: 'constant-vus',
            vus: VUS,              // Number of virtual users from command line
            duration: DURATION,     // Duration from command line
            gracefulStop: '0s'     // Stop immediately
        }
    },
    thresholds: {
        http_req_duration: ['p(99)<3000'],  // 99% of requests must complete within 3s
        http_req_failed: ['rate<0.01'],      // Less than 1% can fail
        success_rate: ['rate>0.95'],        // 95% success rate
        requests_per_second: ['count>100'],   // Minimum RPS
        time_to_first_byte: ['p(95)<1000'],  // 95% of requests should receive first byte within 1s
        page_load_time: ['p(95)<2500']       // 95% of pages should load within 2.5s
    }
};

export function setup() {
    console.log(`
Test Configuration:
------------------
Base URL: ${BASE_URL}
Endpoint: ${ENDPOINT}
Virtual Users: ${VUS}
Duration: ${DURATION}
Cooldown Period: ${COOLDOWN}s
    `);
}

export default function() {
    const startTime = new Date();
    
    const res = http.get(`${BASE_URL}${ENDPOINT}`, {
        headers: { 'Content-Type': 'application/json' },
        timeout: '5s'
    });

    // Record basic metrics
    const duration = new Date() - startTime;
    requestDuration.add(duration);
    requestsPerSecond.add(1);

    // Record TTFB (Time to First Byte)
    timeToFirstByte.add(res.timings.waiting);

    // Record total page load time (includes all phases)
    const totalLoadTime = res.timings.duration;
    pageLoadTime.add(totalLoadTime);
    
    const success = check(res, {
        'endpoint is healthy': (r) => r.status === 200
    });
    successRate.add(success);
}

export function teardown(data) {
    console.log('\nWaiting for metrics to stabilize...');
    sleep(COOLDOWN);
    
    console.log(`
Test Results Summary:
--------------------
Total Requests: ${requestsPerSecond.name}
Success Rate: ${successRate.name}

Response Times:
-------------
Average Response Time: ${requestDuration.name}
95th Percentile: ${requestDuration.name}
99th Percentile: ${requestDuration.name}

Performance Metrics:
------------------
Time to First Byte (TTFB):
  Average: ${timeToFirstByte.name}
  95th Percentile: ${timeToFirstByte.name}

Page Load Time:
  Average: ${pageLoadTime.name}
  95th Percentile: ${pageLoadTime.name}
    `);
}

function validateUrl(url) {
    if (!url) {
        throw new Error(`
BASE_URL is required. 
Usage: k6 run -e BASE_URL=<url> -e VUS=<number> -e DURATION=<time> -e ENDPOINT=<path> -e COOLDOWN=<seconds> login-performance.js
Example: k6 run -e BASE_URL=https://qa.example.com -e VUS=10000 -e DURATION=1s -e COOLDOWN=30 login-performance.js
        `);
    }
    
    const validEnvs = ['qa', 'uat', 'ppe'];
    if (!validEnvs.some(env => url.includes(env))) {
        throw new Error(`Invalid BASE_URL. Must contain one of: ${validEnvs.join(', ')}`);
    }
    
    return url;
}

function validateNumber(value, name, defaultValue) {
    if (!value) return defaultValue;
    const num = parseInt(value);
    if (isNaN(num) || num <= 0) {
        throw new Error(`${name} must be a positive number`);
    }
    return num;
}

function validateDuration(value, defaultValue) {
    if (!value) return defaultValue;
    if (!/^\d+[smh]$/.test(value)) {
        throw new Error('Duration must be in format: <number>[s|m|h] (e.g., 30s, 1m, 1h)');
    }
    return value;
} 