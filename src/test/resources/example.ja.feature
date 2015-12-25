# language: ja

機能: フィーチャファイルからテスト実施結果報告書を生成する
  * テスト仕様書はレビューや差分確認が容易になるため、テキストファイルで作成したい
  * 実施と結果の報告はテキストファイルでは難しいのでひとまずエクセルでまとめたい
  * テスト仕様書から自動で結果報告ファイルを生成したい

  背景:
    前提 example.feature を /tmp/vinegar にコピーする
    前提 /tmp/vinegar/example.feature を UTF-8 にする

  シナリオ: コマンドを実行して結果報告書を生成する
    前提   ホームディレクトリに移動する
    もし   "vinegar /tmp/vinegar/example.feature" を実行する
    ならば /tmp/vinegar に example.xlsx ファイルが存在すること
    ならば 対象ストーリーに「フィーチャファイルからテスト実施結果報告書を生成する」と表示されていること
    ならば 目的／観点に以下が表示されていること:
      """
      * テスト仕様書はレビューや差分確認が容易になるため、テキストファイルで作成したい
      * 実施と結果の報告はテキストファイルでは難しいのでひとまずエクセルでまとめたい
      * テスト仕様書から自動で結果報告ファイルを生成したい
      """
    ならば 事前準備に以下が表示されていること:
      """
      example.feature を /tmp/vinegar にコピーする
      /tmp/vinegar/example.feature を UTF-8 にする
      """
    ならば 各シナリオとステップが表示されていること
      # シナリオは以下の４つ
      # 1. コマンドを実行して同名の結果報告書を生成する
      # 2. オプションで指定したディレクトリにファイルを出力する
      # 3. 存在しないファイルを指定するとエラーを表示する
      # 4. コメントは備考にコピーする
      # ※「ステップが存在しないシナリオは無視する」は表示されないこと
      # 各ステップについては元のファイルと比較して不足がないか確認すること

  シナリオ: オプションで指定したディレクトリにファイルを出力する
    前提   ホームディレクトリに移動する
    もし   "vinegar -o ~/Desktop /tmp/vinegar/example.feature" コマンドを実行する
    ならば ~/Desktop に example.xlsx ファイルが存在すること
    かつ   /tmp/vinegar に example.xlsx ファイルが存在しないこと
    かつ   カレントディレクトリに example.xlsx ファイルが存在しないこと

    もし   "vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir" コマンドを実行する
      # /tmp/vinegar/path/to/deep/dir は存在しないこと
      # 必要であれば rm -rf /tmp/vinegar/path/ で消しておく
    ならば 以下のエラーが表示されること:
      """
      /tmp/vinegar/path/to/deep/dir: No such file or directory
      """
      # 日本語環境の場合はエラーメッセージが日本語になる場合もあります
    かつ   /tmp/vinegar/path/to/deep/dir に example.xlsx ファイルが存在しないこと

    もし   "vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir -f" コマンドを実行する
    ならば エラーが表示されず終了すること
    かつ   /tmp/vinegar/path/to/deep/dir に example.xlsx ファイルが存在すること

  シナリオ: 存在しないファイルを指定するとエラーを表示する
    もし   "vinegar /path/to/dummy.feature" コマンドを実行する
    ならば 以下のエラーが表示されること:
      """
      /path/to/dummy.feature: No such file or directory
      """

  シナリオ: And や But が正しくパースされていること
    もし   ホームディレクトリに移動する
    かつ   "vinegar -o ~/Desktop /tmp/vinegar/example.feature" コマンドを実行する
    かつ   このテキストは上記２ステップと合わせて「When」に記述されていること
    ならば 1. このテキストは「Then」に記述されていること
    かつ   2. このテキストは 1. の次行の「Then」に記述されていること
    ただし 3. このテキストは 3. の次行の「Then」に記述されていること

  シナリオ: ステップが存在しないシナリオは無視する

  シナリオ: コメントは備考にコピーする
    もし   "vinegar /tmp/vinegar/example.feature" を実行する
    ならば 「各シナリオとステップが表示されていること」に備考が設定されていること
    ならば 「"vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir" コマンドを実行する」に備考が設定されていること
    ならば このテストの備考に以下が記入されていること
      #ここはファイル末尾のコメントです
      #備考に入っていますか？
