# RAML 解析 Demo
该项目，主要示例如何使用 RAML 来渲染一个文章页面，也可以将其改造成一个提供 RAML 渲染能力的 SDK 组件。

## 核心代码
* 界面渲染可以参见 `qingmang.raml.article.fragment.ArticleFragment`，它将一个 RAML 的字符串解析渲染成界面
* 而 `qingmang.raml.article.model.HElement` 代表了文章的一个段落，它把 Json 转义成为一个可以直接渲染的对象，其中包含了 `HText` `HImage` 等对象，来实现文本、图片等不同类型段落的解析
