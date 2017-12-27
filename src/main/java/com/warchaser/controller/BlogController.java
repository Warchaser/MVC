package com.warchaser.controller;

import com.warchaser.model.BlogEntity;
import com.warchaser.model.UserEntity;
import com.warchaser.repository.BlogRepository;
import com.warchaser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class BlogController {

    @Autowired
    BlogRepository mBlogRepository;

    @Autowired
    UserRepository mUserRepository;

    /**
     * 查看所有博文
     * */
    @RequestMapping(value = "/admin/blogs", method = RequestMethod.GET)
    public String showBlogs(ModelMap modelMap){
        List<BlogEntity> blogList = mBlogRepository.findAll();
        modelMap.addAttribute("blogList", blogList);

        return "admin/blogs";
    }

    /**
     *  添加博文
     * */
    @RequestMapping(value = "/admin/blogs/add", method = RequestMethod.GET)
    public String addBlog(ModelMap modelMap){
        List<UserEntity> userList = mUserRepository.findAll();
        // 向jsp注入用户列表
        modelMap.addAttribute("userList", userList);
        return "admin/addBlog";
    }

    /**
     * 添加博文，POST请求，重定向为查看博客页面
     * */
    @RequestMapping(value = "/admin/blogs/addP", method = RequestMethod.POST)
    public String addBlogPost(@ModelAttribute("blog") BlogEntity blogEntity){

        System.out.println(blogEntity.getTitle());

        System.out.println(blogEntity.getUserByUserId().getNickname());

        mBlogRepository.saveAndFlush(blogEntity);

        return "redirect:/admin/blogs";
    }

    /**
     * 查看博文详情，默认使用GET方法时，method可以缺省
     * */
    @RequestMapping("/admin/blogs/show/{id}")
    public String showBlogDetail(@PathVariable("id") int id, ModelMap modelMap){

        BlogEntity blog = mBlogRepository.findOne(id);
        modelMap.addAttribute("blog", blog);
        return "admin/blogDetail";
    }

    /**
     * 修改博文内容，页面
     * */
    @RequestMapping("/admin/blogs/update/{id}")
    public String updateBlog(@PathVariable("id") int id, ModelMap modelMap){

        BlogEntity blogEntity = mBlogRepository.findOne(id);
        List<UserEntity> userList = mUserRepository.findAll();
        modelMap.addAttribute("blog", blogEntity);
        modelMap.addAttribute("userList", userList);

        return "admin/updateBlog";
    }

    /**
     * 修改博客内容，POST请求
     * */
    @RequestMapping(value = "/admin/blogs/updateP", method = RequestMethod.POST)
    public String updateBlogP(@ModelAttribute("blogP") BlogEntity blogEntity){

        mBlogRepository.updateBlog(blogEntity.getTitle(),
                blogEntity.getUserByUserId().getId(),
                blogEntity.getContent(),
                blogEntity.getPubDate(),
                blogEntity.getId());

        mBlogRepository.flush();

        return "redirect:/admin/blogs";
    }

    /**
     * 删除博客文章
     * */
    @RequestMapping("/admin/blogs/delete/{id}")
    public String deleteBlog(@PathVariable("id") int id){
        mBlogRepository.delete(id);
        mBlogRepository.flush();
        return "redirect:/admin/blogs";
    }
}
