# 🧑🏻‍🎓 Colaboradores

- [Emesson Horácio](https://github.com/emessonh)
- [Filipe Zaidan](https://github.com/filipezaidan)
- [Lucas Matheus](https://github.com/Casterrr)
- [Maelton Lima](https://github.com/Maelton)

# 📖 Introdução

Projeto realizado na matéria de introdução a redes de computadores para desenvolver um DNS forwarder (aplicação em rede).

# Requisitos para execução

  - JDK 21
  - Instalar o gerenciador de pacotes: Maven
  - Recomendável utilizar intellij como IDE
  - Execute o método main() na classe Main do projeto (Execução padrão)

# Buildar e Executar o Projeto

- Use o Maven para limpar o `/target` e gerar o `.jar` do projeto:

  * `mvn clean package`

- Execute o `.jar do projeto`:

  * `java -jar target/dns-forwarder-1.0-SNAPSHOT.jar`

  Obs.: Altere a especificação do diretório e nome do arquivo .jar ao executar o comando `java -jar` caso você altere as configurações de build padrão do projeto.

# Customizar Porta do Servidor UDP

- Especifique a porta desejada:

  * `java -jar target/dns-forwarder-1.0-SNAPSHOT.jar <porta-customizada>`
  
# Comando da requisição (lado cliente):
  - dig @127.0.0.1 -p 1056 <endereco/url> -> Ip local (@127.0.0.1) na porta 1056 (porta padrão do servidor DNS)
