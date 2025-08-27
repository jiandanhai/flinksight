import datetime
import json
import random


def gen_data(batch=10000, outfile="metric_data.json"):
    now = datetime.datetime.now()
    data = []
    for i in range(batch):
        item = {
            "clusterId": 1,
            "jobId": random.randint(1,10),
            "metricKey": "job_lag",
            "metricValue": random.randint(100, 2000),
            "recordTime": (now - datetime.timedelta(seconds=i)).isoformat(),
            "tenantId": 1
        }
        data.append(item)
    with open(outfile, "w") as f:
        json.dump(data, f)
    print("数据已生成到", outfile)

if __name__ == '__main__':
    gen_data()
