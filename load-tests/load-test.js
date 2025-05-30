// load-test.js
import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 10, // usuarios concurrentes
    duration: '30s', // duración total de la prueba
    thresholds: {
        http_req_duration: ['p(95)<500'], // 95% de las peticiones < 500ms
        http_req_failed: ['rate<0.01'],   // menos del 1% de errores
    },
};


export default function () {
    let res = http.get('http://localhost:8080/actuator/health');
    check(res, {
        'status is 200': (r) => r.status === 200,
    });
    sleep(1);
}
