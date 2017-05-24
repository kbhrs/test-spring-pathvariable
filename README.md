# test-spring-pathvariable
Spring の RestController の PathVariable で取得できる値に関する実験コード。URLエンコーディングされた値はどうなるか？
## 試したこと
- https://github.com/kbhrs/test-spring-pathvariable/blob/master/src/main/java/com/github/kbhrs/urldecode/SampleController.java
- https://github.com/kbhrs/test-spring-pathvariable/blob/master/src/test/java/com/github/kbhrs/urldecode/SampleControllerTest.java

以下のような処理を作って

    @GetMapping(value = "echo/{pathVariable}")
    public String raw(@PathVariable String pathVariable) {
        return pathVariable;
    }

こんな感じのテストを実行したらしたら。getForObject で 404 になった（当たり前だと思っている）

    private static final String UTF8 = StandardCharsets.UTF_8.toString();
    private static final String BUSINESS_VALUE = "aa/bb";
    @Test
    public void testEcho() throws UnsupportedEncodingException {
        String url = "http://localhost:" + port + "/echo/" + BUSINESS_VALUE;
        String message = template.getForObject(url, String.class);
        assertThat(message, is(BUSINESS_VALUE));
    }

仕方がないので、クライアント側で URLEncode した。

    public void testEncode() throws UnsupportedEncodingException {
        String encoded = URLEncoder.encode(BUSINESS_VALUE, UTF8);
        String url = "http://localhost:" + port + "/echo/" + encoded;
        String message = template.getForObject(url, String.class);
        assertThat(message, is(BUSINESS_VALUE));
    }

がこのテストもエラーになった
（ `AssertionError: Expected: is "aa/bb" but: was "aa%2Fbb"` ）

回避策として以下のようなメソッドを作って

    @GetMapping(value = "decode/{pathVariable}")
    public String dencode(@PathVariable String pathVariable) throws UnsupportedEncodingException {
        return URLDecoder.decode(pathVariable, UTF8);
    }

以下のテスト実行したら、テストが通った。

    @Test
    public void testEncodeDecode() throws UnsupportedEncodingException {
        String encoded = URLEncoder.encode(BUSINESS_VALUE, UTF8);
        String decodeUrl = "http://localhost:" + port + "/decode/" + encoded;
        String decoded = template.getForObject(decodeUrl, String.class);
        assertThat(decoded, is(BUSINESS_VALUE));
    }

## できれば。。。
`/echo/` のような実装でうまくいく方法が良いのだが。。。
（＝各メソッドでdecode処理を書きたくない）
