package org.jdiameter.common.impl.app.sh;

import org.jdiameter.api.Answer;
import org.jdiameter.api.Request;
import org.jdiameter.api.sh.events.UserDataRequest;
import org.jdiameter.common.impl.app.AppRequestEventImpl;

public class UserDataRequestImpl extends AppRequestEventImpl implements UserDataRequest {
	public static final int code = 306;



	public UserDataRequestImpl(Answer answer) {
		super(answer);

	}
}
