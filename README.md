Vinegar
=======

フィーチャファイルからテスト実施結果報告書を生成するためのツールです。

## Requirement

* Java SE 6 以降

## Installation

[release](vinegar-cui/releases) ディレクトリから jar ファイルをダウンロードしてください。

## Usage

```
java -jar vinegar-*.jar example/example.feature
```

```
java -jar vinegar-*.jar -o /path/to/project --force example/example.feature
```

```
java -jar vinegar-*.jar --help
```

```
Usage: vinegar [options] file

  -o <outdir> | --out <outdir>
        output directory of generated excel file
  -f | --force
        create output directory (recursively) if directory is not exist
  file
        feature file
  --help
        prints this usage text
```

※ フィーチャファイルは UTF-8 もしくは Shift-JIS で保存してください。

## Contribution

Use sbt console

### Generate idea project

```
sbt gen-idea
```

### Build

```
sbt buildRelease
```

## TODO

- [ ] テーブル形式の引数に対応する
- [ ] シナリオアウトラインに対応する
- [x] 長い文字列の場合でも全体が見えるようにセルの高さを調整する
- [x] 日本語のフィーチャファイルに対応する
- [x] コメント内の # が削除される不具合を修正する
- [x] @pending タグが付いたシナリオは読み飛ばす
