# 動態問卷(後端)
## 使用工具
[前端](https://github.com/Yuuquoi/surveyWeb)：Vue、JavaScript、HTML、SCSS

後端：Java、Spring Boot、MySQL

## 資料庫設計
### 問卷
+ 一份問卷一列，將所有問題存在物件陣列中，轉成字串存入資料庫
+ 不須儲存問卷狀態，運用問卷開始時間和是否發布判斷問卷狀態即可
### 回覆
+ 以問卷編號和手機號碼作為 pk，限制一個手機只能回答同份問卷一次

## 功能
### 問卷
+ 新增
+ 編輯
+ 搜尋
+ 搜尋 ( for 首頁呈現 )
+ 刪除問卷

### 登入

### 答覆
+ 新增答覆
+ 回覆列表
+ 回覆統計
"# Questionnaire" 
