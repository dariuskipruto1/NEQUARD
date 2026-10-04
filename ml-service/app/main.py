from fastapi import FastAPI
from datetime import datetime, timezone
app=FastAPI(title='NEQUARD ML Service',version='0.1.0')
@app.get('/health')
def health(): return {'status':'UP','service':'nequard-ml','timestamp':datetime.now(timezone.utc).isoformat()}
@app.post('/ml/anomaly/detect')
def anomaly(payload:dict): return {'status':'ready','simulated':True,'message':'Inference foundation ready; no unmeasured accuracy is claimed.','input':payload}
