package job;

import com.jfinal.log.Log4jLogger;
import com.jfinal.log.Logger;
import listener.JFinalStartListener;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by zhangyongliang on 15/8/25.test
 */
public class CacheJob  implements Job {

    private static Logger logger = Logger.getLogger(CacheJob.class);

    /**
     * 通过定时任务更新系统缓存数据
     * @param arg0
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        new JFinalStartListener().init();
        logger.info("CacheJob is complete.");
    }

}
