package ${package}.impl.bo.auth;

import ${package}.impl.biz.staff.StaffUser;
import ${package}.impl.biz.staff.StaffUserRepo;
import ${package}.impl.support.beanvalidate.MyValidator;
import ${package}.impl.support.beanvalidate.ValidationError;
import ${package}.intf.bo.auth.BoAuthManager;
import ${package}.intf.bo.auth.BoLoginRequest;
import ${package}.intf.bo.auth.BoLoginResult;
import ${package}.intf.bo.basic.BoConstants;
import ${package}.intf.bo.basic.BoResponse;
import ${package}.utils.lang.MyCodecUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chenjianjx@gmail.com
 */
@Service("boAuthManager")
public class BoAuthManagerImpl implements BoAuthManager {

    @Resource
    MyValidator myValidator;

    @Resource
    StaffUserRepo staffUserRepo;


    @Override
    public BoResponse<BoLoginResult> login(BoLoginRequest request) {
        ValidationError error = myValidator.validateBean(request, BoConstants.NULL_REQUEST_BEAN_TIP);
        if (error.hasErrors()) {
            return BoResponse.userErrResponse(BoConstants.FEC_INVALID_INPUT, error.getNonFieldError(), error.getFieldErrors());
        }

        StaffUser staffUser = staffUserRepo.getStaffUserByUsername(request.getUsername());

        if (staffUser == null) {
            return BoResponse.userErrResponse(BoConstants.FEC_INVALID_INPUT,
                    "Invalid Username", null);
        }

        // compare password
        if (!MyCodecUtils.isPasswordDjangoMatches(request.getPassword(), staffUser.getPassword())) {
            return BoResponse.userErrResponse(BoConstants.FEC_INVALID_INPUT, "Invalid Password", null);
        }


        BoLoginResult result = new BoLoginResult();
        result.setUserId(staffUser.getId());
        result.setUserName(staffUser.getUsername());

        if (!staffUser.isLoggedInOnce()) {
            return BoResponse.errResponseWithData(BoConstants.BEC_FIRST_LOGIN_MUST_CHANGE_PASSWORD, result);
        }


        return BoResponse.success(result);
    }


}
