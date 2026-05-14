from fastapi import Security, HTTPException
from fastapi.security.api_key import APIKeyHeader
from src.core.config import API_KEY

api_key_header = APIKeyHeader(name="X-API-Key", auto_error=True)

def validar_api_key(key: str = Security(api_key_header)):
    if key != API_KEY:
        raise HTTPException(status_code=403, detail="API Key inválida")
