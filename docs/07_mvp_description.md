# 🧬 MVP — Breast Cancer Survival Predictor

---

## 1. Ideia e Justificativa do Projeto

### Descrição da ideia principal

O projeto consiste no desenvolvimento de uma **aplicação mobile de suporte clínico** que utiliza modelos de Machine Learning para prever o risco de agravamento do câncer de mama com base em dados clínicos do paciente.

A partir de variáveis como tamanho do tumor, estágio nodal, status hormonal e grau histológico, o sistema classifica o paciente em perfis de risco — **baixo, moderado ou alto** — fornecendo uma estimativa de sobrevivência e alertas para profissionais de saúde.

### Relevância do projeto

O câncer de mama é a neoplasia mais incidente entre mulheres no Brasil e no mundo. O diagnóstico tardio e a falta de ferramentas de triagem acessíveis impactam diretamente a sobrevida das pacientes. Uma solução preditiva baseada em dados clínicos pode apoiar decisões médicas, especialmente em contextos de recursos limitados.

### Justificativa baseada no problema identificado

A análise exploratória realizada evidenciou que:

- Pacientes com **maior número de linfonodos positivos** apresentam mortalidade significativamente maior (p < 0.001)
- **Tumores maiores** estão diretamente associados ao óbito (p < 0.001)
- O **status hormonal positivo** está correlacionado a maior sobrevivência (p < 0.001)
- O **estágio tumoral (T Stage)** influencia diretamente o prognóstico (p < 0.001)

Esses achados validam a existência de padrões preditivos nos dados e sustentam a viabilidade de um modelo de classificação de risco.

### Público-alvo e escopo

- **Público-alvo primário:** oncologistas, mastologistas e equipes de saúde oncológica
- **Público-alvo secundário:** pacientes que desejam compreender seu perfil de risco
- **Escopo:** aplicação mobile para entrada de dados clínicos, processamento via modelo treinado e exibição do perfil de risco com visualização de fatores determinantes

### Participação interdisciplinar

O projeto foi idealizado com contribuição de estudantes de diferentes áreas, incluindo discussões sobre comunicação de risco ao paciente e acolhimento em saúde — aspectos relevantes para garantir que a ferramenta seja não apenas tecnicamente precisa, mas também humanizada e ética no contexto clínico.

---

## 2. Documentação Inicial — Planejamento da Aplicação

### Tema da Aplicação

Predição de risco de agravamento do câncer de mama com base em variáveis clínicas, utilizando Machine Learning aplicado a dados reais de pacientes.

### Algoritmos que serão utilizados

| Algoritmo | Justificativa |
|---|---|
| Regressão Logística | Baseline interpretável, ideal para variável binária (Alive/Dead) |
| Random Forest | Robusto a outliers, captura relações não-lineares |
| XGBoost | Alta performance em dados tabulares desbalanceados |
| Cox Proportional Hazards | Análise de sobrevivência com variáveis de tempo |

A escolha final será baseada na comparação de métricas após os experimentos.

### Métricas de avaliação escolhidas e justificadas

Dado o **desbalanceamento do dataset** (predominância de pacientes Alive), métricas clássicas como acurácia são insuficientes. As métricas escolhidas são:

| Métrica | Justificativa |
|---|---|
| **AUC-ROC** | Avalia a separação entre classes independente do threshold |
| **F1-Score** | Equilíbrio entre precisão e recall, essencial para classe minoritária |
| **Recall (Sensibilidade)** | Prioridade em minimizar falsos negativos — não identificar um paciente de alto risco é crítico |
| **Precisão** | Evitar alarmes falsos que causem ansiedade desnecessária |
| **Log-Loss** | Avalia a qualidade das probabilidades geradas pelo modelo |

### Cronograma com metas e prazos

| Fase | Atividade | Prazo |
|---|---|---|
| **Fase 1** | EDA e análise exploratória | ✅ Concluído |
| **Fase 2** | Pré-processamento e feature engineering | Semana 1–2 |
| **Fase 3** | Treinamento e comparação de modelos | Semana 3–4 |
| **Fase 4** | Avaliação, ajuste de hiperparâmetros | Semana 5 |
| **Fase 5** | Desenvolvimento do MVP mobile | Semana 6–8 |
| **Fase 6** | Testes, validação e apresentação | Semana 9–10 |

### Metodologia ágil

O projeto utiliza **Kanban** como metodologia ágil, com colunas:

- `Backlog` → `Em progresso` → `Revisão` → `Concluído`

As tarefas são gerenciadas via **Trello**, com sprints semanais e revisão de progresso a cada entrega parcial.

### Definição mobile

A aplicação será desenvolvida para plataforma **mobile**, com as seguintes características:

- Interface de entrada de dados clínicos (formulário guiado)
- Processamento do modelo via API backend (Python/FastAPI)
- Exibição do perfil de risco com indicador visual (baixo / moderado / alto)
- Gráfico de fatores mais relevantes para o resultado (SHAP values simplificados)
- Compatível com Android e iOS via **React Native** ou **Flutter**

---

## 4. Notebook com Análise Exploratória dos Dados

### Tecnologias utilizadas no projeto

| Camada | Tecnologia | Finalidade |
|---|---|---|
| **Análise de dados** | Python, Pandas, NumPy | Manipulação e transformação dos dados |
| **Visualização** | Matplotlib, Seaborn | Gráficos e análises visuais |
| **Estatística** | SciPy, Lifelines | Testes de hipótese e análise de sobrevivência |
| **Machine Learning** | Scikit-learn, XGBoost | Treinamento e avaliação dos modelos |
| **Interpretabilidade** | SHAP | Explicação das predições do modelo |
| **Backend da API** | FastAPI (Python) | Exposição do modelo como serviço REST |
| **Frontend mobile** | React Native / Flutter | Interface do usuário mobile |
| **Ambiente de desenvolvimento** | Jupyter Notebook, PyCharm | EDA e desenvolvimento |
| **Versionamento** | Git + GitHub | Controle de versão e colaboração |

### Viabilidade técnica

A implementação é tecnicamente viável pelos seguintes fatores:

- O dataset utilizado é público, estruturado e contém 4.024 registros com 16 variáveis clínicas relevantes, volume suficiente para treinamento de modelos supervisionados
- Todas as ferramentas são **open-source e gratuitas**, sem necessidade de infraestrutura paga na fase de desenvolvimento
- A análise exploratória confirmou **padrões estatisticamente significativos** entre as variáveis e o desfecho, validando a hipótese de que um modelo preditivo é treinável com esses dados
- A execução local não demanda GPU ou hardware especializado para os algoritmos selecionados
- A arquitetura proposta (modelo + API REST + app mobile) é modular e permite desenvolvimento incremental

### Análise exploratória

A EDA foi realizada integralmente em notebook Jupyter (`nootebooks/EDA_breast_cancer.ipynb`) e cobriu:

1. Compreensão da estrutura do dataset (`shape`, `info`, `describe`)
2. Verificação de dados nulos e duplicados
3. Estatísticas descritivas das variáveis numéricas
4. Identificação de outliers via método IQR
5. Matriz de correlação e análise de relações entre variáveis
6. Visualização de variáveis categóricas com heatmaps de proporção
7. Análise de distribuições por grupos (histogramas, boxplots, pairplot)
8. Curvas de Kaplan-Meier por Estrogen Status e T Stage
9. Testes de hipóteses estatísticas (t-test, ANOVA, log-rank)

Os principais achados estão documentados em `docs/04_exploratory_analysis.md` e `docs/05_hypotheses.md`.
