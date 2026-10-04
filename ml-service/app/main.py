from fastapi import FastAPI
from pydantic import BaseModel, Field
from statistics import mean, pstdev
app=FastAPI(title="NEQUARD ML Service",version="1.0.0")
class Series(BaseModel):
    values:list[float]=Field(min_length=1)
@app.get("/health")
def health(): return {"status":"UP","service":"nequard-ml","models":"baseline"}
@app.post("/ml/anomaly/detect")
def anomaly(s:Series):
    m=mean(s.values); sd=pstdev(s.values) if len(s.values)>1 else 0.0
    score=0.0 if sd==0 else abs((s.values[-1]-m)/sd)
    return {"anomaly":score>=3.0,"z_score":score,"baseline_mean":m,"baseline_stddev":sd,"method":"z-score"}
@app.post("/ml/failure/predict")
def failure(s:Series):
    if len(s.values)<2: return {"probability":0.0,"risk":"INSUFFICIENT_DATA","window":"UNKNOWN","contributing_factors":[]}
    slope=(s.values[-1]-s.values[0])/max(1,len(s.values)-1)
    probability=max(0.0,min(0.99,0.5+0.08*slope))
    risk="HIGH" if probability>=.75 else "MEDIUM" if probability>=.5 else "LOW"
    return {"probability":probability,"risk":risk,"window":"FUTURE_WINDOW_REQUIRES_TRAINED_MODEL","contributing_factors":[{"factor":"observed_trend","direction":"INCREASING" if slope>0 else "STABLE_OR_DECREASING"}],"model_status":"BASELINE_HEURISTIC"}
@app.post("/ml/root-cause/analyze")
def root_cause(metrics:dict[str,float]):
    loss=metrics.get("packet_loss",0); latency=metrics.get("latency",0); cpu=metrics.get("cpu",0)
    if loss>=20:return {"probable_cause":"LINK_OR_UPSTREAM_DEGRADATION","confidence":0.6,"evidence":["packet_loss>=20"],"next_step":"Inspect affected interfaces and upstream path"}
    if cpu>=90:return {"probable_cause":"DEVICE_RESOURCE_PRESSURE","confidence":0.6,"evidence":["cpu>=90"],"next_step":"Inspect processes and recent configuration changes"}
    if latency>=200:return {"probable_cause":"LATENCY_DEGRADATION","confidence":0.5,"evidence":["latency>=200ms"],"next_step":"Trace path and compare neighboring links"}
    return {"probable_cause":"NO_DOMINANT_SIGNAL","confidence":0.2,"evidence":[],"next_step":"Collect additional telemetry"}
