package com.example.todoapp.exception;

public class CustomException extends Exception {
  // コンストラクタ: デフォルト
  public CustomException() {
    super();
  }

  // コンストラクタ: エラーメッセージを含む
  public CustomException(String message) {
    super(message);
  }

  // コンストラクタ: エラーメッセージと原因となる例外を含む
  public CustomException(String message, Throwable cause) {
    super(message, cause);
  }

  // コンストラクタ: 原因となる例外を含む
  public CustomException(Throwable cause) {
    super(cause);
  }
}
