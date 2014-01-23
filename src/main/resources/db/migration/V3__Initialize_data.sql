/*
  Load Concurrent Programs as Objects
 */
insert into sm_objects(id,code,name,object_type,object_source) (
                select sm_object_s.nextval, p.concurrent_program_name, cp.user_concurrent_program_name, 'AOL-CP', e.execution_file_name target
                from fnd_concurrent_programs_tl cp, fnd_application_tl a, fnd_concurrent_programs p, FND_EXECUTABLES e,  fnd_lookups l1
                where cp.application_id = a.application_id
                and cp.concurrent_program_id = p.concurrent_program_id
                and p.executable_id = e.executable_id
                and l1.lookup_type = 'CP_EXECUTION_METHOD_CODE'
                and l1.lookup_code = e.execution_method_code
                and concurrent_program_name like 'XXVS%');

/*
  Load Tables as Objects
 */

insert into sm_objects(id,code,name,object_type,object_source) (
select sm_object_s.nextval, table_name, table_name, 'DB-TABLE', table_name || '.sql'
from all_tables
where table_name like 'XXVS%' and owner = 'XXVS' )

/*
  Load Views as Objects
 */

insert into sm_objects(id,code,name,object_type,object_source) (
select sm_object_s.nextval, table_name, table_name, 'DB-TABLE', table_name || '.sql'
from all_tables
where table_name like 'XXVS%' and owner = 'XXVS' )

/*
  Load Packages as Objects
 */

insert into sm_objects(id,code,name,object_type,object_source) (
select sm_object_s.nextval, object_name, object_name, 'DB-PKH', object_name || '.pks'
from all_objects
where object_type like 'PACKAGE'  and object_name like 'XXVS%' )

/*
  Load Package Bodies as Objects
 */
insert into sm_objects(id,code,name,object_type,object_source) (
select sm_object_s.nextval, object_name, object_name, 'DB-PKB', object_name || '.pkb'
from all_objects
where object_type like 'PACKAGE%BODY%'  and object_name like 'XXVS%' )

/*
  Load Procedures and Functions Bodies as Objects
 */
insert into sm_objects(id,code,name,object_type,object_source) (
select sm_object_s.nextval, object_name, object_name, 'DB-PLS', object_name || '.pls'
from all_objects
where object_type like 'PROCEDURE'  and object_name like 'XXVS%' )

insert into sm_objects(id,code,name,object_type,object_source) (
select sm_object_s.nextval, object_name, object_name, 'DB-PLS', object_name || '.pls'
from all_objects
where object_type like 'FUNCTION'  and object_name like 'XXVS%' )

insert into sm_components( id, component_num, name, component_type, module,track,description,sm_stage, sm_stage_date)
(
select sm_component_s.nextval,
      'O2C' || '-' || RTRIM(substr(p.concurrent_program_name,6,3),'_') || '-' || LPAD(to_char(rownum),3,'0'),
      cp.user_concurrent_program_name,
      'Process',
      RTRIM(substr(p.concurrent_program_name,6,3),'_'),
      'O2C',
      cp.user_concurrent_program_name,
      'PROD',
      trunc(cp.creation_date)
      from fnd_concurrent_programs_tl cp, fnd_application_tl a, fnd_concurrent_programs p, FND_EXECUTABLES e,  fnd_lookups l1
      where cp.application_id = a.application_id
      and cp.concurrent_program_id = p.concurrent_program_id
      and p.executable_id = e.executable_id
      and l1.lookup_type = 'CP_EXECUTION_METHOD_CODE'
      and l1.lookup_code = e.execution_method_code
      and concurrent_program_name like 'XXVS%'
)