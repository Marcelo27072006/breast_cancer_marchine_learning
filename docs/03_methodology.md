# 🧪 Metodologia

A análise foi conduzida utilizando a linguagem **Python**, com bibliotecas amplamente utilizadas na área de ciência de dados, permitindo uma abordagem estruturada, reprodutível e eficiente.

---

## 🛠️ Ferramentas Utilizadas

- **Pandas**  
  Manipulação e análise de dados estruturados.

- **NumPy**  
  Operações matemáticas e manipulação de arrays.

- **Matplotlib**  
  Criação de gráficos e visualizações.

- **Seaborn**  
  Visualizações estatísticas avançadas.

- **SciPy**  
  Aplicação de testes estatísticos, como o **Teste t de Student**.

---

## 🔍 Etapas da Análise

A análise exploratória de dados (EDA) foi realizada seguindo as etapas abaixo:

1. Leitura e inspeção inicial do dataset  
2. Verificação dos tipos de dados  
3. Análise estatística descritiva  
4. Cálculo de medidas de dispersão  
5. Identificação de outliers  
6. Análise de variáveis categóricas  
7. Visualização gráfica dos dados  
8. Aplicação de testes estatísticos  

---

## 📊 Método para Identificação de Outliers

Para a detecção de valores discrepantes, foi utilizado o método do **Intervalo Interquartil (IQR)**.

### 🔹 Cálculo do IQR

$$
IQR = Q_3 - Q_1
$$

Onde:

- $Q_1$ representa o primeiro quartil (25%)  
- $Q_3$ representa o terceiro quartil (75%)

---

### 🔹 Definição dos Limites

Os limites para identificação de outliers são definidos como:

$$
LI = Q_1 - 1.5 \cdot IQR
$$

$$
LS = Q_3 + 1.5 \cdot IQR
$$

Onde:

- **LI (Limite Inferior)** identifica outliers baixos  
- **LS (Limite Superior)** identifica outliers altos  

Valores abaixo de $LI$ ou acima de $LS$ são considerados outliers.

---