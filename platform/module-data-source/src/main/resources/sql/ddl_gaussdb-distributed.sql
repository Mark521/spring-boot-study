create table source_info (
  id decimal(11) not null,
  name varchar(50),
  remark varchar(255),
  source_type decimal(11),
  source_key varchar(50),
  url varchar(1024),
  host varchar(100),
  port decimal(11),
  database_name varchar(50),
  catalog_name varchar(50),
  schema_name varchar(50),
  username varchar(50),
  password varchar(50),
  credential varchar(1024),
  credential_url varchar(1024),
  driver_file varchar(1024),
  driver_file_url varchar(1024),
  driver_class varchar(100),
  initial_size decimal(11),
  max_active decimal(11),
  min_idle decimal(11),
  max_wait decimal(11),
  validation_query varchar(200),
  custom_pro varchar(4000),
  custom_url_pro varchar(4000),
  is_inset decimal(2),
  is_enabled decimal(2),
  public_source decimal(2),
  app_ids varchar(1024),
  create_user decimal(11),
  create_time timestamp,
  update_user decimal(11),
  update_time timestamp,
  delete_user decimal(11),
  delete_time timestamp,
  data_state varchar(50)
) distribute by hash(id);
alter table source_info add constraint source_info_pk primary key (id);

comment on table source_info is '数据源信息';
comment on column source_info.id is '主键';
comment on column source_info.name is '数据源名称';
comment on column source_info.remark is '备注';
comment on column source_info.source_type is '数据源类型';
comment on column source_info.source_key is '数据源唯一标示';
comment on column source_info.url is '连接地址';
comment on column source_info.host is 'IP或主机名';
comment on column source_info.port is '端口';
comment on column source_info.database_name is '库名';
comment on column source_info.catalog_name is 'catalog';
comment on column source_info.schema_name is 'schema';
comment on column source_info.username is '用户名';
comment on column source_info.password is '密码';
comment on column source_info.credential is '密码凭证在默认文件服务器中的Key';
comment on column source_info.credential_url is '密码凭证在数据源文件服务器中的URL';
comment on column source_info.driver_file is '驱动文件在默认文件服务器中的Key';
comment on column source_info.driver_file_url is '驱动文件在数据源文件服务器中的URL';
comment on column source_info.driver_class is '驱动类';
comment on column source_info.initial_size is '初始化连接池大小';
comment on column source_info.max_active is '最大活动连接';
comment on column source_info.min_idle is '最小空闲连接';
comment on column source_info.max_wait is '最大等待时长（毫秒）';
comment on column source_info.validation_query is '校验连接是否有效的SQL';
comment on column source_info.custom_pro is '自定义属性，JSON格式';
comment on column source_info.custom_url_pro is 'JSON格式，自定义文件属性在数据源文件服务器中的URL保存在这里';
comment on column source_info.is_inset is '是否内置数据源';
comment on column source_info.is_enabled is '是否启用';
comment on column source_info.public_source is '是否公开的数据源';
comment on column source_info.app_ids is '未公开数据源，允许使用该数据源的应用ID，逗号分隔';
comment on column source_info.create_user is '创建人';
comment on column source_info.create_time is '创建时间';
comment on column source_info.update_user is '修改人';
comment on column source_info.update_time is '修改时间';
comment on column source_info.delete_user is '删除人';
comment on column source_info.delete_time is '删除时间';
comment on column source_info.data_state is '数据的逻辑状态（1：有效，非1：无效）';

create sequence source_info_seq minvalue 1 maxvalue 99999999999 increment by 1 start with 10000 cache 20 ;
