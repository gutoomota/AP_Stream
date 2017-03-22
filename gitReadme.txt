>Inicializar projeto git
git init

>Adicionar reposit�rio novo ao projeto
git remote add origin https://github.com/<nomeDoProjeto>.git

>Clonar um projeto:
git clone https://github.com/<nomeDoProjeto>.git

>Criar novo branch
git checkout -b "nome_do_branch"	

>Ir para branch que foram feitas as altera��es, n�o � necess�rio se voc� j� estiver nesse branch.
git checkout nome_do_branch

>Adiciona as novas altera��es no branch.
git add -A

>Cria um pacote das altera��es para que sejam enviadas ao reposit�rio do github.
git commit -m "nome_do_commit"

>To delete a Commit
git reset --hard origin/master

>Envia o c�digo ao github.
git push -u origin nome_do_branch

>Para mudar para o branch master, se quiser voltar ao outro branch s� � usar o mesmo c�digo modificando o nome do branch pelo de seu interesse.
git checkout master

>Atualiza projeto com o que est� no git
git pull