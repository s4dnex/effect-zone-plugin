# JouTak Tempplate
Template plugin for JouTak <ins>(joutak.ru-mc.ru)</ins>

Что нужно сделать:
1) склонить проект себе по ssh
2) открыть в любой удобной ide(vs code | inteliji idea)
3) разобраться с расположением папочек и синтаксисом котлина, прописать базовую логику, которая описана в issues.
4) собрать его с помощью gradle 
5) запустить у себя на компе Purpur сервер и закинуть на него этот плагин
6) подключиться со своего майна, проверить, что оно заработало
7) отправить пулл реквест обратно на гитхаб, закрыв issue
если всё работает, то пулл реквест автоматически сбилдит такой же jarник, который будет так же работать на твоём локальном сервере
сначала всё можно писать в main файлик, но потом команды и лисенеры ивентов выдели в два отдельных файлика. Главное - не стесняйся спрашивать!

# Полезное:
## Building JAR

1. Add a project from repo into Intellij IDEA
2. Use "Run" button on the top right to build jar\
`[snapshot]` builds go to `plugins/` folder of test server \
`[release]` builds go to `build/` folder of the project
