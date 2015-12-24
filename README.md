Vinegar
=======

フィーチャファイルからテスト実施結果報告書を生成するためのツールです。

## Requirement

* Java SE 6 以降

## Installation

`release` ディレクトリの `vinegar-*.jar` をダウンロードしてください。

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

- [ ] @pending タグが付いたシナリオは読み飛ばす
- [ ] テーブル形式の引数に対応する
- [ ] 日本語のフィーチャファイルに対応する
