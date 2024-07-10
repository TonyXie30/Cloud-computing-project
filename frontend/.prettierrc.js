module.exports = {
  printWidth: 80, // 单行长度,超过则自动换行
  tabWidth: 2,
  singleQuote: true, // 使用单引号
  endOfLine: 'auto', // 配置换行符格式为auto
  bracketSpacing: true, // 在对象前后添加空格-eg: { foo: bar }
  insertPragma: false, // 在已被preitter格式化的文件顶部加上标注
  rangeStart: 0,
  requirePragma: false, // 无需顶部注释即可格式化
  trailingComma: 'none', // 多行时尽可能打印尾随逗号
};
