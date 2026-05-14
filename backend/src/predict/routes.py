from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from src.database.connection import get_db
from src.predict.schemas import PredicaoInput, PredicaoOutput
from src.predict.service import realizar_predicao
from src.predict import repositories
from src.core.security import validar_api_key

router = APIRouter()

@router.post("/", response_model=PredicaoOutput)
def criar_predicao(
    input: PredicaoInput,
    db: Session = Depends(get_db),
    _: str = Depends(validar_api_key)
):
    resultado = realizar_predicao(input.model_dump())
    predicao  = repositories.salvar_predicao(db, input, resultado)
    return predicao

@router.get("/", response_model=list[PredicaoOutput])
def listar_predicoes(
    db: Session = Depends(get_db),
    _: str = Depends(validar_api_key)
):
    return repositories.listar_predicoes(db)

@router.get("/{predicao_id}", response_model=PredicaoOutput)
def buscar_predicao(
    predicao_id: int,
    db: Session = Depends(get_db),
    _: str = Depends(validar_api_key)
):
    predicao = repositories.buscar_predicao(db, predicao_id)
    if not predicao:
        raise HTTPException(status_code=404, detail="Predição não encontrada")
    return predicao