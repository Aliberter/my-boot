package cn.aliberter.boot.modules.demo.queue;

import cn.aliberter.boot.modules.user.entity.SysUser;
import cn.hutool.core.lang.Console;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author : aliberter
 * @version : 1.0
 */
public class BlockingQueueDemo {

    public static void main(String[] args) {

        BlockingQueue<SysUser> blockingQueue = new ArrayBlockingQueue<>(10);

        int size = 10;

        for (int i = 0; i < size; i++) {
            try {
                blockingQueue.put(new SysUser().setUsername("张三" + i).setPassword("123"));
            } catch (Exception e) {
                //ignore
            }
        }

        try {
            Console.log(blockingQueue.take());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
