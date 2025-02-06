import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 10,
    duration: '30s',
};

export default function () {
    const res = http.post('https://api.example.com/login', {
        username: 'testuser',
        password: 'testpass',
    });

    check(res, {
        'is status 200': (r) => r.status === 200,
        'is login successful': (r) => r.json().success === true,
    });

    sleep(1);
} 