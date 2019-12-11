#### 一、基础篇

###### 1、生命周期

- created：实例创建完成后调用，此阶段完成数据检测，但未挂载，$el还不能用需要初始化处理一些数据
- mounted：el挂载到实例上后调用，一般第一个业务逻辑在此
- beforeDestroy：实例销毁前调用。主要解绑一些使用addEventListener监听的事件

###### 2、过滤器

```javascript
{{ date | formateDate }}
formatDate 方法需要在 new Vue({ filters：{ formatDate:function(){},}})定义
```

###### 3、指令和事件

```js
v-bind 简化为 " : "
v-on   简化为 " @ "
```

###### 4、计算属性（computed）

有getter和setter方法

```js
computed:{
    fullName:{
        get:function(){
            return this.a + b;
        };
        set:function(value){
            this.a = value;
            this.b = value;
        }
    }
}
```





