package com.company;

public class Main {

    public static void main(String[] args) {
      Opt<String> opt = Opt.error().onOk(o -> Opt.error()).orElse(o -> Opt.ok("hello"));
      System.out.println(opt.get());

      Result result = Result.ok.onError(r -> Result.ok).orElse(r -> Result.error);
      System.out.println(result.isError());

      System.out.println(opt.details());
      System.out.println("---------------");
      System.out.println(result.details());
    }
}
