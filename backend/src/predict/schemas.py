from pydantic import BaseModel, Field
from datetime import datetime
from typing import Literal, List
from uuid import UUID

class VariavelImpacto(BaseModel):
    variavel: str
    valor:    str
    hipotese: str
    sintomas: List[str]
    fonte:    str

class PredicaoInput(BaseModel):
    paciente_nome: str = Field(..., example="João Silva")

    age:            int     = Field(..., ge=20, le=100, example=52)
    race:           Literal["White", "Black", "Other"] = Field(..., example="White")
    marital_status: Literal["Married", "Single", "Divorced", "Widowed", "Separated"] = Field(..., example="Married")

    tumor_size:     int     = Field(..., ge=1, le=200, example=30)
    t_stage:        Literal["T1", "T2", "T3", "T4"] = Field(..., example="T2")
    grade:          Literal[1, 2, 3, 4] = Field(..., example=2)
    differentiate:  Literal["Well differentiated", "Moderately differentiated", "Poorly differentiated", "Undifferentiated"] = Field(..., example="Moderately differentiated")
    a_stage:        Literal["Regional", "Distant"] = Field(..., example="Regional")
    sixth_stage:    Literal["IIA", "IIB", "IIIA", "IIIB", "IIIC"] = Field(..., example="IIA")

    n_stage:                 Literal["N1", "N2", "N3"] = Field(..., example="N1")
    regional_node_examined:  int = Field(..., ge=0, le=100, example=14)
    reginol_node_positive:   int = Field(..., ge=0, le=100, example=2)

    estrogen_status:     Literal["Positive", "Negative"] = Field(..., example="Positive")
    progesterone_status: Literal["Positive", "Negative"] = Field(..., example="Positive")

class PredicaoOutput(BaseModel):
    id:             UUID
    paciente_nome:  str
    predicao:       str
    probabilidade:  float
    nivel_risco:    str
    criado_em:      datetime
    aviso:          str = "As hipóteses apresentadas são baseadas em literatura científica e têm caráter informativo. Não substituem avaliação médica especializada."
    variaveis_impacto: List[VariavelImpacto] = []

    class Config:
        from_attributes = True

#Segue a mesma ordem do models:
#Dados pacientes, dados sobre cliente, dados do tumor, dados linfonodos, dados status hormonal e os dados quanto ao resultado previsto