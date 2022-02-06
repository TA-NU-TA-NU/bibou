package com.example.bibou.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//認証の為の設定を行うクラス
//WebSecurityConfigurerAdapterを継承

@Configuration
@EnableWebSecurity
public class MainSecurityConfigurer extends WebSecurityConfigurerAdapter{

    @Override
    public void configure(WebSecurity web) throws Exception{
        //無視するreq
        web.ignoring().antMatchers("/css/**","/img/**","/js/**");
    }

    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //メモリ内認証情報定義。要は定義したユーザー情報である。
        //なぜ{noop}が頭にないとcurlコマンドで入れたパスが通らす5XXエラーになるのか謎
        auth.inMemoryAuthentication()
            .withUser("user").password("{noop}password").roles("USER")
            .and()
            .withUser("admin").password("{noop}password").roles("USER","ADMIN");
    }

    @Override
    protected void configure(HttpSecurity https)throws Exception {
        https
            .httpBasic()
            .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/usr").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/usr/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/usr/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/usr").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/usr/**").hasRole("ADMIN")
                .and()
                .csrf().disable()
                .formLogin().disable();
    }
}