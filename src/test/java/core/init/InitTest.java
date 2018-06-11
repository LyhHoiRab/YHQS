package core.init;

import com.yhqs.YHQSApplication;
import com.yhqs.core.init.FunctionInit;
import com.yhqs.core.init.MenuInit;
import com.yhqs.core.init.RoleInit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = YHQSApplication.class)
@EnableAutoConfiguration
public class InitTest{

    @Autowired
    private RoleInit roleInit;

    @Autowired
    private MenuInit menuInit;

    @Autowired
    private FunctionInit functionInit;

    @Test
    public void roleInit(){
        roleInit.init();
    }

    @Test
    public void menuInit(){
        menuInit.init();
    }

    @Test
    public void functionInit(){
        functionInit.init();
    }
}
