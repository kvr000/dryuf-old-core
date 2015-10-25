/*
 * Dryuf framework
 *
 * ----------------------------------------------------------------------------------
 *
 * Copyright (C) 2000-2015 Zbyněk Vyškovský
 *
 * ----------------------------------------------------------------------------------
 *
 * LICENSE:
 *
 * This file is part of Dryuf
 *
 * Dryuf is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 *
 * Dryuf is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dryuf; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * @author	2000-2015 Zbyněk Vyškovský
 * @link	mailto:kvr@matfyz.cz
 * @link	http://kvr.matfyz.cz/software/java/dryuf/
 * @link	http://github.com/dryuf/
 * @license	http://www.gnu.org/licenses/lgpl.txt GNU Lesser General Public License v3
 */

package net.dryuf.security.web.spring;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import net.dryuf.core.UiContext;
import net.dryuf.srvui.PageContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import net.dryuf.core.CallerContext;
import net.dryuf.security.UserAccount;
import net.dryuf.security.UserAccountDetails;
import net.dryuf.security.bo.UserAccountBo;
import net.dryuf.security.web.AuthenticationFrontend;
import net.dryuf.srvui.Request;
import net.dryuf.srvui.spring.SpringCallerContext;
import net.dryuf.web.jee.JeeWebRequest;


public class SpringAuthenticationFrontend extends java.lang.Object implements AuthenticationFrontend
{
	@Named("org.springframework.security.authenticationManager")
	@Inject
	AuthenticationManager		authenticationManager;

	@Inject
	UserAccountBo			userAccountBo;

	@Override
	public int			authenticateUserPassword(@NotNull PageContext pageContext, String username, String password)
	{
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		authenticationToken.setDetails(new WebAuthenticationDetails(((JeeWebRequest) pageContext.getRequest()).getServletRequest()));
		((JeeWebRequest) pageContext.getRequest()).getServletRequest().getSession();
		UserAccount userAccount = new UserAccount();
		userAccount.setUsername(username);
		userAccount.setPassword(password);
		Collection<String> roles = new LinkedHashSet<String>();
		int err = userAccountBo.login(userAccount, roles, ((JeeWebRequest) pageContext.getRequest()).getServletRequest().getSession().getId(), "");
		if (err != 0)
			return err;
		try {
			Authentication authentication = new UsernamePasswordAuthenticationToken(new UserAccountDetails(userAccount, userAccountBo), "", Collections2.transform(roles,
				new Function<String, GrantedAuthority>() {
					@Override
					public GrantedAuthority		apply(String role)
					{
						return new SimpleGrantedAuthority(role);
					}
				}));
			//authenticationManager.authenticate(authenticationToken);
			//authentication.setAuthenticated(true);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			((JeeWebRequest) pageContext.getRequest()).getServletRequest().getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
			return 0;
		}
		catch (AuthenticationException ex) {
			return err;
		}
	}

	@Override
	public CallerContext		initCallerContext(@NotNull Request request)
	{
		return SpringCallerContext.createFromServletContext(((JeeWebRequest)request).getServletRequest().getServletContext());
	}

	@Override
	public void			logout(@NotNull PageContext pageContext)
	{
		pageContext.invalidateSession();
		pageContext.getCallerContext().loggedOut();
	}

	@Override
	public void			setEffectiveUserId(@NotNull PageContext pageContext, Object userId)
	{
		pageContext.getSession().setAttribute(CallerContext.class.getName()+".effectiveUserId", userId);
	}

	@Override
	public void			resetRoles(@NotNull PageContext pageContext, Set<String> newRoles)
	{
		Authentication oldAuthentication = SecurityContextHolder.getContext().getAuthentication();
		Authentication authentication = new UsernamePasswordAuthenticationToken(oldAuthentication.getPrincipal(), "", Collections2.transform(newRoles,
				new Function<String, GrantedAuthority>() {
					@Override
					public GrantedAuthority		apply(String role)
					{
						return new SimpleGrantedAuthority(role);
					}
				}));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		((JeeWebRequest)pageContext.getRequest()).getServletRequest().getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
	}

	@Override
	public void			setTranslationLevel(@NotNull PageContext pageContext, int translationLevel)
	{
		pageContext.getSession().setAttribute(UiContext.class.getName()+".translationLevel", translationLevel);
	}

	@Override
	public void			setTiming(@NotNull PageContext pageContext, boolean timing)
	{
		pageContext.getSession().setAttribute(UiContext.class.getName()+".timing", timing);
	}
}
