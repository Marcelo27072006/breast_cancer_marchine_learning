from sqlalchemy import Column, Integer, String, Float, DateTime
from datetime import datetime
from src.database.connection import Base
import uuid
from sqlalchemy.dialects.postgresql import UUID

class Predicao(Base):
    __tablename__ = "predicoes"

    id              = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4, index=True)
    paciente_nome   = Column(String, nullable=False)
    criado_em       = Column(DateTime, default=datetime.utcnow)

    age             = Column(Integer, nullable=False)
    race            = Column(String, nullable=False)
    marital_status  = Column(String, nullable=False)

    tumor_size      = Column(Integer, nullable=False)
    t_stage         = Column(String, nullable=False)
    grade           = Column(Integer, nullable=False)
    differentiate   = Column(String, nullable=False)
    a_stage         = Column(String, nullable=False)
    sixth_stage     = Column(String, nullable=False)

    n_stage                  = Column(String, nullable=False)
    regional_node_examined   = Column(Integer, nullable=False)
    reginol_node_positive    = Column(Integer, nullable=False)

    estrogen_status     = Column(String, nullable=False)
    progesterone_status = Column(String, nullable=False)

    predicao      = Column(String, nullable=False)
    probabilidade = Column(Float, nullable=False)
    nivel_risco   = Column(String, nullable=False)


#Dados pacientes, dados sobre cliente, dados do tumor, dados linfonodos, dados status hormonal e os dados quanto ao resultado previsto