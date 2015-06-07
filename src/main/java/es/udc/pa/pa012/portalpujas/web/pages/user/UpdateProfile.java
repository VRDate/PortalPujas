package es.udc.pa.pa012.portalpujas.web.pages.user;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa012.portalpujas.model.userprofile.UserProfile;
import es.udc.pa.pa012.portalpujas.model.userservice.UserProfileDetails;
import es.udc.pa.pa012.portalpujas.model.userservice.UserService;
import es.udc.pa.pa012.portalpujas.web.pages.Index;
import es.udc.pa.pa012.portalpujas.web.services.AuthenticationPolicy;
import es.udc.pa.pa012.portalpujas.web.services.AuthenticationPolicyType;
import es.udc.pa.pa012.portalpujas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class UpdateProfile {

	@Property
	private String firstName;

	@Property
	private String lastName;

	@Property
	private String email;

	@SessionState(create = false)
	private UserSession userSession;

	@Inject
	private UserService userService;

	void onPrepareForRender() throws InstanceNotFoundException {

		UserProfile userProfile;

		userProfile = userService.findUserProfile(userSession
				.getUserProfileId());
		firstName = userProfile.getFirstName();
		lastName = userProfile.getLastName();
		email = userProfile.getEmail();

	}

	Object onSuccess() throws InstanceNotFoundException {

		userService.updateUserProfileDetails(userSession.getUserProfileId(),
				new UserProfileDetails(firstName, lastName, email));
		userSession.setFirstName(firstName);
		return Index.class;

	}

}