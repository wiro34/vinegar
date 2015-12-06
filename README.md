Vinegar
=======

フィーチャファイルからテスト実施結果報告書を生成するためのツールです。

## Requirement

* Node.js >= 4.2.1

## Installation

`npm install -g https://stash.sddiv.net:8443/scm/tool/vinegar.git`

※ SSL認証がオレオレのためNPMのSSL認証を無効化する必要があります。<br>
　 代替手段としては git clone した後で、`npm install -g ~/vinegar` のようにディレクトリを指定してインストールしてください。

## 使い方

`vinegar example/example.feature`

`vinegar -o /tmp example/example.feature`

## Contribution

### run SBT console

```
git clone ssh://git@stash.sddiv.net:7999/tool/vinegar.git
npm install
sbt
```

### Generate idea project

```
sbt gen-idea
```

### Build

```
npm run build
```

## TODO List

* @pending タグが付いたシナリオは読み飛ばす
* テーブル形式の引数に対応する

