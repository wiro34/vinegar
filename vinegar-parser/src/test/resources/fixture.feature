Feature: テスト用フィーチャファイル
  * フィーチャの説明文は「目的／観点」に設定されます。
   * もちろん複数行も可能です。
 * インデントは最短マッチで削除されます。

  # フィーチャ直下にコメントを書くと備考になります。
  # 必要に応じて使い分けてください。

  Background: 事前準備
    Given Background には事前準備を記述します。
    Given ユーザを作成する、初期データを投入するなどテスト実施前に必要なステップを記述してください。

  Scenario: シナリオ
    Given これは Given ステップです。
      # Given ステップのコメントはステップ内に改行して記入されます。
    When これは When ステップです。
    And When ステップが複数あると、Given が行結合されて横に並びます。
      # When のコメントも各ステップ内に改行して記入されます。
    Then これは Then ステップです。
    And もちろん、複数行テキストも使えます:
      """
      複数行テキストやテーブルを使う場合はステップの末尾を : にします。
      長い文字列や、複数行に渡る入力値を表現するのに便利です。
      """
      # パラメータ付きのステップにコメントを付けることも可能です
      # Then ステップのコメントは「備考」欄に記入されます

    And 複数行テキストは """ もしくは ``` で囲みます:
      ```
      こんな感じです。
      ```
    Given これは２つ目の Given ステップです。
    Then When をすっ飛ばすことも可能です。
      # 例えば、ページを開いたら〜が表示されていること、なんてテストは When を使わないかもしれません。
    When テーブルも利用可能です
    And ユーザを作成する:
      | username | password |
      | test01   | pass01   |
      | test02   | pass02   |
      | hoge     | foobar   |

      # メールアドレスは適当で可

    When "test01" ユーザでログインする
    Then ログインできること
    But ただし〜できないこと、ただし〜でないことなどは But を使います。

  Scenario Outline: シナリオアウトラインのサンプル
    Given ログイン画面を表示する
    When ユーザ名に <username> と入力する
    When パスワードに <password> と入力する
    When "ログイン" ボタンをクリックする
    Then <message> と表示されていること

    Examples:
      | username | password | message               |
      | invalid  | pass01   | ユーザ名、またはパスワードが違います。   |
      | test01   | invalid  | ユーザ名、またはパスワードが違います。   |
      | expired  | pass01   | アカウントの有効期限が切れています。    |
      | freezed  | pass01   | あなたのアカウントは現在凍結されています。 |