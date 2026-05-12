# 🧹 Pré-processamento — Breast Cancer Survival Predictor

## Visão Geral

O pré-processamento é a etapa responsável por preparar o dataset bruto para uso nos modelos de Machine Learning. Seu objetivo é garantir que os dados estejam limpos, consistentes, numericamente representados e balanceados — sem introduzir viés ou vazamento de informações entre treino e teste.

---

## 📂 Arquivos

| Entrada | Saída                                             |
|---|---------------------------------------------------|
| `data/data_Raw/Breast_Cancer.csv` | `data/processed_data/Breast_Cancer_processed.csv` |
| | `data/processed_data/X_train.csv`                      |
| | `data/processed_data/X_test.csv`                       |
| | `data/processed_data/y_train.csv`                      |
| | `data/processed_data/y_test.csv`                       |

---

## 🔢 Etapas

### 1 — Carregamento e Inspeção
Carregamento do CSV bruto original com inspeção inicial da estrutura — shape, tipos e primeiros registros. Nenhum dado é modificado nesta etapa.

---

### 2 — Limpeza de Dados
Correção de inconsistências estruturais no dataset:

- **Remoção de duplicatas** — 1 linha duplicada encontrada e removida
- **Padronização de colunas** — espaços removidos e substituídos por underline (`T Stage ` → `T_Stage`)
- **Limpeza de valores** — espaços invisíveis removidos dos valores categóricos (`'Single '` → `'Single'`)
- **Correção do Grade** — valor `'anaplastic; Grade IV'` padronizado para `'4'` para manter consistência com os demais valores da coluna

---

### 3 — Encoding de Variáveis Categóricas
Conversão de todas as variáveis categóricas para representações numéricas. Feita antes do split para garantir mapeamento consistente entre treino e teste.

**Ordinal Encoding** — variáveis com ordem clínica real:

| Variável | Mapeamento |
|---|---|
| `Grade` | 1 (melhor) → 4 (anaplásico) |
| `T_Stage` | T1 = 1 → T4 = 4 |
| `N_Stage` | N1 = 1 → N3 = 3 |
| `6th_Stage` | IIA = 1 → IIIC = 5 |
| `differentiate` | Well = 1 → Undifferentiated = 4 |

**Label Encoding** — variáveis sem ordem natural:
`Race`, `Marital_Status`, `A_Stage`, `Estrogen_Status`, `Progesterone_Status`

**Target:**
`Status` → `Alive = 0` | `Dead = 1`

---

### 4 — Remoção de Data Leakage
A variável `Survival_Months` foi removida do dataset de treinamento.

Ela representa quantos meses o paciente sobreviveu após o diagnóstico — uma informação retrospectiva que não existe no momento da predição real. Mantê-la causaria **data leakage**: o modelo aprenderia a usar o tempo de sobrevivência para prever o desfecho, sem aprender nada das variáveis clínicas reais.

> A variável foi utilizada apenas na EDA para análise exploratória.

---

### 5 — Salvamento do Dataset Central
O dataset completo, após limpeza e encoding e antes do split e normalização, é salvo como arquivo de referência central.

```
data/processed_data/Breast_Cancer_processed.csv
```

Útil para análises futuras, visualizações adicionais ou retreinamento com estratégias diferentes. As variáveis numéricas ainda estão na escala original.

---

### 6 — Divisão Treino/Teste
Separação do dataset em 80% treino e 20% teste, feita antes da normalização e do SMOTE.

| Parâmetro | Valor | Motivo |
|---|---|---|
| `test_size` | 0.2 | 20% para teste, 80% para treino |
| `random_state` | 42 | Reprodutibilidade — mesma divisão a cada execução |
| `stratify` | y | Mantém proporção Alive/Dead nos dois conjuntos |

---

### 7 — Normalização
Aplicação do **MinMaxScaler** nas variáveis numéricas contínuas, colocando todos os valores na escala de 0 a 1.

Variáveis normalizadas: `Age`, `Tumor_Size`, `Regional_Node_Examined`, `Reginol_Node_Positive`

Feita **depois do split** para evitar data leakage:
- `fit_transform` no treino → aprende min e max apenas dos dados de treino
- `transform` no teste → aplica os mesmos parâmetros sem reaprender

---

### 8 — Balanceamento com SMOTE
O dataset original possui proporção de **5,5:1** entre Alive e Dead. Sem correção, o modelo tenderia a sempre prever Alive e atingir ~85% de acurácia — clinicamente inútil.

O **SMOTE (Synthetic Minority Oversampling Technique)** gera exemplos sintéticos da classe minoritária (Dead) interpolando entre vizinhos reais.

| | Alive | Dead |
|---|---|---|
| Antes do SMOTE | 2.725 | 493 |
| Após o SMOTE | 2.725 | 2.725 |

> Aplicado **apenas no treino**. O teste permanece com a distribuição real.

---

### 9 — Salvamento dos Arquivos Separados
Os quatro arquivos finais são salvos para uso direto pelos notebooks dos modelos:

```
data/processed_data/
├── X_train.csv   ← features de treino (normalizadas + SMOTE)
├── X_test.csv    ← features de teste (normalizadas, dados reais)
├── y_train.csv   ← target de treino (balanceado com SMOTE)
└── y_test.csv    ← target de teste (dados reais, desbalanceado)
```

Carregamento nos notebooks dos modelos:
```python
X_train = pd.read_csv('../data/processed_data/X_train.csv')
X_test  = pd.read_csv('../data/processed_data/X_test.csv')
y_train = pd.read_csv('../data/processed_data/y_train.csv').squeeze()
y_test  = pd.read_csv('../data/processed_data/y_test.csv').squeeze()
```

---

## ⚠️ Decisões Técnicas

| Decisão | Motivo |
|---|---|
| Encoding antes do split | Garante mapeamento consistente entre treino e teste |
| Normalização depois do split | Evita que o scaler aprenda estatísticas do teste |
| SMOTE só no treino | O teste deve refletir a distribuição real do mundo |
| `Survival_Months` removida | Data leakage — informação indisponível no momento da predição |
| `stratify=y` no split | Preserva proporção de classes nos dois conjuntos |

---

## 🛠️ Dependências

```
pandas
numpy
matplotlib
scikit-learn
imbalanced-learn
```
