| 大分類     | 中分類                                       | 小分類       | 結果                                                               |
| ---------- | -------------------------------------------- | ------------ | ------------------------------------------------------------------ |
| ログイン済 | ログインユーザーと紐付け済み                 |              | 元のログイン後画面に戻る                                           |
|            | ログインユーザーとは別のユーザーと紐付け済み |              |
|            | そのユーザーとも紐付け済み                   |              | 紐付け処理して、元のログイン後画面に戻る                           |
| 未ログイン | 紐付けユーザーあり                           | ログイン     | そのままログイン後画面に遷移                                       |
|            |                                              | サインアップ | すでにユーザーがある旨を伝えて、ログイン画面を表示                 |
|            | 紐付けユーザーなし                           | ログイン     | ログイン失敗として、ログイン画面にエラーメッセージ表示             |
|            |                                              | サインアップ | （ユーザー情報入力画面に遷移して入力した後、）ログイン後画面に遷移 |
