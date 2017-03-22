>Inicializar projeto git
git init

>Adicionar repositório novo ao projeto
git remote add origin https://github.com/<nomeDoProjeto>.git

>Clonar um projeto:
git clone https://github.com/<nomeDoProjeto>.git

>Criar novo branch
git checkout -b "nome_do_branch"	

>Ir para branch que foram feitas as alterações, não é necessário se você já estiver nesse branch.
git checkout nome_do_branch

>Adiciona as novas alterações no branch.
git add -A

>Cria um pacote das alterações para que sejam enviadas ao repositório do github.
git commit -m "nome_do_commit"

>To delete a Commit
git reset --hard origin/master

>Envia o código ao github.
git push -u origin nome_do_branch

>Para mudar para o branch master, se quiser voltar ao outro branch só é usar o mesmo código modificando o nome do branch pelo de seu interesse.
git checkout master

>Atualiza projeto com o que está no git
git pull