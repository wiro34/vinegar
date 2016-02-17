Feature: フィーチャファイルからテスト実施結果報告書を生成する
* テスト仕様書はレビューや差分確認が容易になるため、テキストファイルで作成したい
* 実施と結果の報告はテキストファイルでは難しいのでひとまずエクセルでまとめたい
* テスト仕様書から自動で結果報告ファイルを生成したい

  # ここにコメントを書くと備考になります。
  # もちろん複数のコメントを書くことも
  # 可能です。

  Background:
    Given example.feature を /tmp/vinegar にコピーする
    Given /tmp/vinegar/example.feature を UTF-8 にする

  Scenario: コマンドを実行して結果報告書を生成する
    Given ホームディレクトリに移動する
    When "vinegar /tmp/vinegar/example.feature" を実行する
    Then /tmp/vinegar に example.xlsx ファイルが存在すること
    Then 対象ストーリーに「フィーチャファイルからテスト実施結果報告書を生成する」と表示されていること
    Then 目的／観点に以下が表示されていること:
      """
      * テスト仕様書はレビューや差分確認が容易になるため、テキストファイルで作成したい
      * 実施と結果の報告はテキストファイルでは難しいのでひとまずエクセルでまとめたい
      * テスト仕様書から自動で結果報告ファイルを生成したい
      """
    Then 事前準備に以下が表示されていること:
      """
      example.feature を /tmp/vinegar にコピーする
      /tmp/vinegar/example.feature を UTF-8 にする
      """
    Then 各シナリオとステップが表示されていること
      # シナリオは以下の４つ
      # 1. コマンドを実行して同名の結果報告書を生成する
      # 2. オプションで指定したディレクトリにファイルを出力する
      # 3. 存在しないファイルを指定するとエラーを表示する
      # 4. コメントは備考にコピーする
      # ※「ステップが存在しないシナリオは無視する」は表示されないこと
      # 各ステップについては元のファイルと比較して不足がないか確認すること

  Scenario: オプションで指定したディレクトリにファイルを出力する
    Given ホームディレクトリに移動する
    When "vinegar -o ~/Desktop /tmp/vinegar/example.feature" コマンドを実行する
    Then ~/Desktop に example.xlsx ファイルが存在すること
    And  /tmp/vinegar に example.xlsx ファイルが存在しないこと
    And  カレントディレクトリに example.xlsx ファイルが存在しないこと

    Given ホームディレクトリに移動する
    When "vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir" コマンドを実行する
      # /tmp/vinegar/path/to/deep/dir は存在しないこと
      # 必要であれば rm -rf /tmp/vinegar/path/ で消しておく
    Then 以下のエラーが表示されること:
      """
      /tmp/vinegar/path/to/deep/dir: No such file or directory
      """
      # エラーメッセージが日本語の可能性もあり
    And  /tmp/vinegar/path/to/deep/dir に example.xlsx ファイルが存在しないこと

    When "vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir -f" コマンドを実行する
    Then エラーが表示されず終了すること
    And  /tmp/vinegar/path/to/deep/dir に example.xlsx ファイルが存在すること

  Scenario: 存在しないファイルを指定するとエラーを表示する
    When "vinegar /path/to/dummy.feature" コマンドを実行する
    Then 以下のエラーが表示されること:
      """
      /path/to/dummy.feature: No such file or directory
      """

  Scenario: And や But が正しくパースされていること
    When ホームディレクトリに移動する
    And  "vinegar -o ~/Desktop /tmp/vinegar/example.feature" コマンドを実行する
    And  このテキストは上記２ステップと合わせて「When」に記述されていること
    Then 1. このテキストは「Then」に記述されていること
    And  2. このテキストは 1. の次行の「Then」に記述されていること
    But  3. このテキストは 3. の次行の「Then」に記述されていること
    Given このテキストは次の行の「Given」
    Then 1. このテキストは「Then」に記述されていること
    When ホームディレクトリに移動する
    Then 1. このテキストは「Then」に記述されていること
    When ホームディレクトリに移動する
    Then 1. このテキストは「Then」に記述されていること

  Scenario: ステップが存在しないシナリオは無視する

  Scenario: コメントは備考にコピーする
    When "vinegar /tmp/vinegar/example.feature" を実行する
    Then 「各シナリオとステップが表示されていること」に備考が設定されていること
    Then 「"vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir" コマンドを実行する」に備考が設定されていること
    Then このテストの備考に以下が記入されていること
      #ここはファイル末尾のコメントです
      #備考に入っていますか？
