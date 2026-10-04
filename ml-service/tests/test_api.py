from fastapi.testclient import TestClient
from app.main import app
client=TestClient(app)
def test_health(): assert client.get("/health").json()["status"]=="UP"
def test_anomaly(): assert client.post("/ml/anomaly/detect",json={"values":[1,1,1,10]}).status_code==200
