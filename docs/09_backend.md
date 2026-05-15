# 🖥️ Backend — Breast Cancer Survival Predictor

## Visão Geral

API REST desenvolvida em **FastAPI** com **PostgreSQL**, responsável por receber os dados clínicos do paciente, aplicar o pré-processamento, realizar a predição via modelo de Machine Learning e retornar o resultado com hipóteses clínicas baseadas em literatura científica.

---

## 📂 Estrutura

```
backend/
├── main.py                  
├── requirements.txt         
├── .env                     
├── .env.example             
└── src/
    ├── core/
    │   ├── config.py       
    │   └── security.py      
    ├── database/
    │   └── connection.py   
    └── predict/
        ├── models.py        
        ├── schemas.py       
        ├── service.py       
        ├── repositories.py  
        └── routes.py        
```

---

## 🔌 Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/` | Health check — verifica se a API está no ar |
| `POST` | `/predicao/` | Realiza predição e salva no banco |
| `GET` | `/predicao/` | Lista todas as predições realizadas |
| `GET` | `/predicao/{id}` | Busca uma predição específica por ID |

Todos os endpoints (exceto `/`) exigem autenticação via **API Key** no header `X-API-Key`.

---

## 🔐 Autenticação

A API utiliza autenticação por **API Key** — sem login ou cadastro de usuários. A chave é configurada no `.env` e enviada pelo cliente no header de cada request.

```
X-API-Key: sua-chave-secreta
```

Sem a chave correta o backend retorna `403 Forbidden`. Essa abordagem protege os dados clínicos dos pacientes sem adicionar complexidade de sessão ao MVP.

---

## 📥 Entrada — `POST /predicao/`

```json
{
  "paciente_nome": "João Silva",
  "age": 52,
  "race": "White",
  "marital_status": "Married",
  "tumor_size": 30,
  "t_stage": "T2",
  "grade": 2,
  "differentiate": "Moderately differentiated",
  "a_stage": "Regional",
  "sixth_stage": "IIA",
  "n_stage": "N1",
  "regional_node_examined": 14,
  "reginol_node_positive": 2,
  "estrogen_status": "Positive",
  "progesterone_status": "Positive"
}
```

---

## 📤 Saída — `POST /predicao/`

```json
{
  "id": "id_hash",
  "paciente_nome": "João Silva",
  "predicao": "Alive",
  "probabilidade": 25.0,
  "nivel_risco": "baixo",
  "criado_em": "2026-05-15T01:46:50.664776",
  "aviso": "As hipóteses apresentadas são baseadas em literatura científica e têm caráter informativo. Não substituem avaliação médica especializada.",
  "variaveis_impacto": []
}
```

Para casos de risco moderado ou alto, `variaveis_impacto` é preenchido:

```json
"variaveis_impacto": [
  {
    "variavel": "Reginol_Node_Positive",
    "valor": "12",
    "hipotese": "Alto comprometimento de linfonodos está associado a fadiga persistente, dor linfática localizada e maior risco de linfedema.",
    "sintomas": ["fadiga persistente", "dor linfática", "linfedema", "distress emocional"],
    "fonte": "Fu et al., Integrative Cancer Therapies, 2022"
  }
]
```

---

## ⚙️ Fluxo de Predição

```
App Kotlin
    ↓ POST /predicao/ + X-API-Key
Validação da API Key
    ↓
Validação dos dados de entrada (Pydantic)
    ↓
Pré-processamento (service.py)
    ├── Label Encoding das categóricas nominais (label_mappings.joblib)
    ├── Ordinal Encoding das categóricas ordinais (mapeamento fixo)
    └── Normalização das numéricas (scaler.joblib)
    ↓
Predição (random_forest.joblib)
    ↓
Definição do nível de risco
    ├── < 30% → baixo
    ├── 30–60% → moderado
    └── > 60% → alto
    ↓
Geração de hipóteses clínicas (CLINICAL_IMPACT)
    └── Ativadas apenas para risco moderado e alto
    ↓
Salvamento no PostgreSQL
    ↓
Resposta ao app
```

---

## 🗄️ Banco de Dados

Tabela `predicoes` no PostgreSQL:

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | Integer PK | Identificador único |
| `paciente_nome` | String | Nome do paciente |
| `age` | Integer | Idade |
| `race` | String | Raça |
| `marital_status` | String | Estado civil |
| `tumor_size` | Integer | Tamanho do tumor (mm) |
| `t_stage` | String | Estágio T do tumor |
| `grade` | Integer | Grau do tumor |
| `differentiate` | String | Diferenciação celular |
| `a_stage` | String | Estágio A |
| `sixth_stage` | String | 6º estadiamento |
| `n_stage` | String | Estágio N (linfonodos) |
| `regional_node_examined` | Integer | Linfonodos examinados |
| `reginol_node_positive` | Integer | Linfonodos positivos |
| `estrogen_status` | String | Status estrogênio |
| `progesterone_status` | String | Status progesterona |
| `predicao` | String | Alive ou Dead |
| `probabilidade` | Float | % de probabilidade de óbito |
| `nivel_risco` | String | baixo, moderado ou alto |
| `criado_em` | DateTime | Timestamp da predição |

> `variaveis_impacto` não é salvo no banco — é gerado dinamicamente a cada request com base nos dados de entrada e na probabilidade retornada pelo modelo.

---

## 🤖 Artefatos do Modelo

Localizados em `data-science/models/`:

| Arquivo | Descrição |
|---|---|
| `scaler.joblib` | MinMaxScaler fitado no treino |
| `label_mappings.joblib` | Mapeamentos do Label Encoding |
| `random_forest.joblib` | Modelo Random Forest treinado |
| `naive_bayes.joblib` | Modelo Naive Bayes treinado |
| `xgboost.joblib` | Modelo XGBoost treinado |

---

## 🐳 Como rodar com Docker

# Na raiz do projeto
docker-compose up --build -d

# Verificar se está rodando
curl http://localhost:8000/

## 🛠️ Tecnologias

| Tecnologia | Função |
|---|---|
| FastAPI | Framework da API REST |
| SQLAlchemy | ORM para comunicação com o banco |
| PostgreSQL | Banco de dados relacional |
| Pydantic | Validação de dados de entrada e saída |
| Joblib | Carregamento dos modelos treinados |
| Python-dotenv | Leitura das variáveis de ambiente |
| Uvicorn | Servidor ASGI |
