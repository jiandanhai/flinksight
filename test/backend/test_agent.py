import requests, time, random, datetime

def mock_metric():
    return [{
        "clusterId": 1,
        "jobId": 1,
        "metricKey": "job_lag",
        "metricValue": random.randint(100, 1000),
        "recordTime": datetime.datetime.now().isoformat(),
        "tenantId": 1
    }]

if __name__ == "__main__":
    while True:
        metrics = mock_metric()
        try:
            resp = requests.post("http://localhost:8080/api/metric/batch", json=metrics)
            print("Push:", resp.status_code, resp.text)
        except Exception as e:
            print("Error:", e)
        time.sleep(2)
