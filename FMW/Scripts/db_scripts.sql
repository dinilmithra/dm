
show parameter open_cursors
alter session set container=pdborcl;
alter system set open_cursors=1000 scope=both;
alter session set container=cdb$root;
select name, con_id, value from v$system_parameter where name='open_cursors';
shutdown immediate;