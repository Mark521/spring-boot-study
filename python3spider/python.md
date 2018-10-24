```py
selenium ，chromedriver 模拟浏览器访问
http://npm.taobao.org/mirrors/chromedriver/2.36/
from selenium import webdriver
driver = webdriver.Chrome()
driver.get("baidu.com")


phantomjs不需要浏览器模拟
lxml//xpath的解析方式
beautifulsoup4//网页解析库
pyquery
pymysql
pymongo
redis
flas//web
django
jupyter//记事本，
splash//页面渲染
pyV8、ghost.py
```

### 爬虫基本原理解析

1、请求网站并提取数据的自动化程序

### Urllib：python内置HTTP请求库

##### urllib.request：请求模块

requests，

handler（代理）

```python
urilib.request.ProxyHandler({})
```

cookies

```python
cookie = http.cookiejar.CookieJar()
cookie = http.cookiejar.LWPCookieJar()//lwp cookie2.0
filename = ""
cookie = http.cookiejar.CookieJar(filename)

handler = urllib.request.HTTPCookieProcessor(cookie)
opener = urllib.request.build_opener(handler)
response = opender.open("www.baidu.com")

cookie.save()
cookie.load()
```

##### urllib.error：异常处理模块

##### urllib.parse：URL解析模块

```python
urlparse:解析URL
urlunparse:数组生成URL
urljoin：URL拼合
urlencode:字典转换为请求参数
```

##### urllib.robotparser：robots.txt解析模块



### Requests库

基于urlib3

```python
//请求方式
requests.post()
requests.get()
//解析json
response.json()
//二进制数据
response.content()//可获取字节流
//添加header

response = request.get()
response.cookies
//会话保持
s = requests.session()
s.get(”www.baidu.com“)
//证书验证
requests.get("", cert=('',''))
//代理设置
proxies = {
    'http':'',
    'https':''
}
requests.get('', proxies = proxies)
//socks代理
pip3 install request[socks]
//认证设置
import requests.auth import HTTPBasicAuth
r = requests.get("", auth=HTTPBasicAuth('user', '123'))

```

### 正则表达式

RE库提供实现

```python
re.match('', content)
re.group()//匹配到得值
re.span()//范围
非贪婪匹配加 ？号
. 不匹配换行符
匹配模式 re.s .* 匹配任意得字符

re.search//找一个
re.findall//找所有
re.sub()//将正则到的数据替换为指定字符并返回
```

### BeautifulSoup库

```python
find_all("ul")
find_all(attrs={"id":"1"})
find_all(attrs=(id:1)//属性
find_all(text="")//内容
select('.class ')//css选择器
ul['id']
ul.attrs[id]
```

### PyQuery库

```python
from pyquery import PyQuery as pq
doc = pg("str")
```

### Selenium库

```python
自动化测试工具，支持多种浏览器，爬虫中主要用来做Javascript渲染
brower = webdriver.Chrome()
try:
    brower.get('www.baidu.com')
    input=brower.find_element_by_id('kw')
    input.send_keys('Python')
    input.send_keys(Keys.ENTER)
    wait = WebDriverWait(brower,10)
    wait.until(EC.presence_of_element_located((By.ID,'content_left')))
    print(browder.current_url)
    
元素交互操作
input = brower.find_element_by_id('q')
input.send_keys('iphone')
time.sleep(1)
input.clear()
input.send_keys('ipad')
button=brower.find_element_by_class_name('btn_search')
button.click

//将动作添加到工作链中
from selenium.webdriver import ActionChains

//执行javascript
brower.execute_script('')

//获取元素信息
input.get_attribute('')
input.text()
```

