#!/bin/bash
host=192.168.35.111
src=/backup/
des=web
user=root
/usr/local/inotify-3.14/bin/inotifywait -mrq --timefmt '%d/%m/%y %H:%M' --format '%T %w%f%e' -e modify,delete,create,attrib $src \
|while read files
do
/usr/bin/rsync -vzrtopg --delete --progress --password-file=/etc/rsync.pwd22 $src $user@$host::$des
echo "${files} was rsynced" >>/tmp/rsync.log 2>&1
done
