
show parameter open_cursors
alter session set container=pdborcl;
alter system set open_cursors=1000 scope=both;
alter session set container=cdb$root;
select name, con_id, value from v$system_parameter where name='open_cursors';
shutdown immediate;

select name, open_mode from v$pdbs;

ALTER PLUGGABLE DATABASE ORCLPDB OPEN;
alter pluggable database all open;

alter pluggable database ORCLPDB save state;
alter pluggable database all save state;
alter pluggable database all except pdb_name1, pdb_name2 save state;

show parameter open_cursors;
alter system set open_cursors=1000 scope=both;
