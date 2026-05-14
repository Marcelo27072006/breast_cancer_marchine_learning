from sqlalchemy.orm import Session
from src.predict.models import Predicao
from src.predict.schemas import PredicaoInput

def salvar_predicao(db: Session, input: PredicaoInput, resultado: dict) -> Predicao:
    predicao = Predicao(
        paciente_nome        = input.paciente_nome,
        age                  = input.age,
        race                 = input.race,
        marital_status       = input.marital_status,
        tumor_size           = input.tumor_size,
        t_stage              = input.t_stage,
        grade                = input.grade,
        differentiate        = input.differentiate,
        a_stage              = input.a_stage,
        sixth_stage          = input.sixth_stage,
        n_stage              = input.n_stage,
        regional_node_examined  = input.regional_node_examined,
        reginol_node_positive   = input.reginol_node_positive,
        estrogen_status      = input.estrogen_status,
        progesterone_status  = input.progesterone_status,
        predicao             = resultado["predicao"],
        probabilidade        = resultado["probabilidade"],
        nivel_risco          = resultado["nivel_risco"]
    )

    db.add(predicao)
    db.commit()
    db.refresh(predicao)
    return predicao

def listar_predicoes(db: Session) -> list[Predicao]:
    return db.query(Predicao).order_by(Predicao.criado_em.desc()).all()

def buscar_predicao(db: Session, predicao_id: int) -> Predicao:
    return db.query(Predicao).filter(Predicao.id == predicao_id).first()