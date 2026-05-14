from fastapi import FastAPI
from src.predict.routes import router as predicao_router
from src.database.connection import Base, engine

Base.metadata.create_all(bind=engine)

app = FastAPI(
    title="Breast Cancer Survival Predictor API",
    description="API para predição de risco em pacientes com câncer de mama",
    version="1.0.0"
)

app.include_router(predicao_router, prefix="/predicao", tags=["Predição"])

@app.get("/")
def health_check():
    return {"status": "ok", "message": "API rodando"}
