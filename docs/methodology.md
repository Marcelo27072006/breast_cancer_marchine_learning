# 🧪 Metodologia

A análise foi conduzida utilizando Python com bibliotecas voltadas para ciência de dados.

## Ferramentas Utilizadas

* Pandas
* NumPy
* Matplotlib
* Seaborn
* SciPy

## Etapas da análise

1. leitura e inspeção do dataset
2. verificação de tipos de dados
3. análise descritiva
4. medidas de dispersão
5. identificação de outliers
6. análise de variáveis categóricas
7. visualização gráfica
8. testes estatísticos

## Método para outliers

Foi utilizado o método do **IQR (Intervalo Interquartil)**:

$$
IQR = Q3 - Q1
$$

IQR = Q_3 - Q_1

Limites:

$$
LI = Q1 - 1.5 \cdot IQR
$$

LI = Q_1 - 1.5\cdot IQR

$$
LS = Q3 + 1.5 \cdot IQR
$$

LS = Q_3 + 1.5\cdot IQR
