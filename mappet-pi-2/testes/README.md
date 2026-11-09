# Testes do módulo desktop

Programa de teste das regras do MVP (CT01 a CT23 da seção 6.9 da documentação). Ele chama direto as classes `Validador`, `FotoUtil`, `Sessao` e os controllers, nos caminhos que não dependem do banco, e compara a mensagem obtida com a esperada.

| Arquivo | Conteúdo |
|---|---|
| `TesteMappet.java` | Programa de teste |
| `img/` | Imagens de teste: PNG e JPG válidos e um texto renomeado para `.jpg` |
| `resultado.txt` | Saída da última execução (23 aprovados, 0 reprovados) |
| `execucao_testes.png` | Captura da execução, usada na documentação |

## Como rodar

Com o projeto do `desktop` já compilado (`mvn package`), a partir desta pasta:

```
javac -encoding UTF-8 -cp ../desktop/target/mappet-desktop.jar -d out TesteMappet.java
java -cp ../desktop/target/mappet-desktop.jar:out TesteMappet img
```

No Windows, troque `:` por `;` no segundo comando.

O CT16 (foto maior que 5 MB) cria um arquivo temporário na hora, para o repositório não precisar guardar uma imagem grande.

Os testes que dependem do Oracle (CT31 a CT36) e os de interface (CT24 a CT30) estão descritos na documentação. As evidências dos testes de interface estão em [`../telas/desktop`](../telas/desktop).
