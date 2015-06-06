package es.udc.pa.pa012.portalpujas.web.pages.bid;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import es.udc.pa.pa012.portalpujas.model.bid.Bid;
import es.udc.pa.pa012.portalpujas.model.bidservice.BidBlock;
import es.udc.pa.pa012.portalpujas.model.bidservice.BidService;
import es.udc.pa.pa012.portalpujas.web.services.AuthenticationPolicy;
import es.udc.pa.pa012.portalpujas.web.services.AuthenticationPolicyType;
import es.udc.pa.pa012.portalpujas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class FindUserBids {

	private final static int BIDS_PER_PAGE = 10;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");

	@Property
	@SessionState(create = false)
	private UserSession userSession;

	@Property
	private Bid bid;

	private int startIndex = 0;
	private BidBlock bidBlock;

	@Inject
	private BidService bidService;

	@Inject
	private Locale locale;

	@InjectComponent
	private Zone loopZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	public Format getFormat() {
		return NumberFormat.getInstance(locale);
	}

	public String getBidTime() {
		Calendar d = bid.getBidTime();
		return format.format(d.getTime());
	}

	public List<Bid> getBids() {
		System.out.println(bidBlock.getBids().size());
		return bidBlock.getBids();
	}

	public void setBidBlock(BidBlock bidBlock) {
		this.bidBlock = bidBlock;
	}

	Object onActionFromRefreshZonePrev() throws InstanceNotFoundException{
		if (startIndex - BIDS_PER_PAGE >= 0) {
			this.startIndex = startIndex - BIDS_PER_PAGE;
			bidBlock = bidService.checkBidsStatus(
					userSession.getUserProfileId(), startIndex, BIDS_PER_PAGE);
		}
		// Here we can do whatever updates we want, then return the content we
		// want rendered.
		return request.isXHR() ? loopZone.getBody() : null;
	}

	Object onActionFromRefreshZoneNext() throws InstanceNotFoundException{
		if (bidBlock.getExistMoreBids()) {
			this.startIndex = startIndex + BIDS_PER_PAGE;
			bidBlock = bidService.checkBidsStatus(
					userSession.getUserProfileId(), startIndex, BIDS_PER_PAGE);
		}
		// Here we can do whatever updates we want, then return the content we
		// want rendered.
		return request.isXHR() ? loopZone.getBody() : null;
	}

	public Object[] getPreviousLinkContext() {

		if (startIndex - BIDS_PER_PAGE >= 0) {
			return new Object[] { startIndex - BIDS_PER_PAGE,
					userSession.getUserProfileId(), startIndex };
		} else {
			return null;
		}

	}

	public Object[] getNextLinkContext() {

		if (bidBlock.getExistMoreBids()) {
			return new Object[] { startIndex + BIDS_PER_PAGE,
					userSession.getUserProfileId(), startIndex };
		} else {
			return null;
		}

	}

	public boolean getWinner() {
		if (bid.getInstantWinner() != null)
			return true;
		else
			return false;
	}

	public boolean getActualPrice() {
		if (bid.getInstantPrice().compareTo(new BigDecimal("0")) == 1)
			return true;
		else
			return false;
	}

	int onPassivate() {
		return startIndex;
	}

	void onActivate(int startIndex) throws InstanceNotFoundException{
		this.startIndex = startIndex;
		bidBlock = bidService.checkBidsStatus(userSession.getUserProfileId(),
				startIndex, BIDS_PER_PAGE);
	}
}
