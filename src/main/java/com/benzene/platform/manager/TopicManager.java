package com.benzene.platform.manager;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.benzene.platform.entity.Concept;
import com.benzene.platform.entity.Topic;
import com.benzene.util.LogFactory;
import com.benzene.util.dao.CommonDAO;
import com.benzene.util.enums.State;
import com.benzene.util.request.GetAbstractReq;
import com.benzene.util.sessionfactory.SqlSessionFactory;

@Service
public class TopicManager {

	@Autowired
	private CommonDAO commonDAO;

	@Autowired
	private LogFactory logfactory;

	@Autowired
	private SqlSessionFactory sqlSessionfactory;

	@SuppressWarnings("static-access")
	private Logger logger = logfactory.getLogger(TopicManager.class);

	public Topic addOrUpdateTopic(Topic topic) {
		Session session = sqlSessionfactory.getSessionFactory().openSession();
		Transaction transaction = session.getTransaction();

		Long id = topic.getId();
		try {
			transaction.begin();
			if (id == null) {
				commonDAO.saveEntity(topic, session);
			} else {
				Topic topic1 = (Topic) commonDAO.getEntity(id, null, Topic.class, session);
				topic = addUpdates(topic1, topic);
				commonDAO.updateEntity(topic, session);
			}
			transaction.commit();
		} finally {
			session.close();
		}

		return topic;
	}

	public Topic getTopic(Long id, State state) {
		return commonDAO.getEntity(id, state, Topic.class);
	}

	public List<Topic> getTopics(GetAbstractReq req) {
		return commonDAO.getEntities(Topic.class, req, null);
	}

	public void updateTopics(List<Topic> slist) {
		commonDAO.updateEntities(slist, Topic.class.getSimpleName());
	}

	public Concept addConcept(Long id, Concept concept) {
		Session session = sqlSessionfactory.getSessionFactory().openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			Topic topic = (Topic) commonDAO.getEntity(id, null, Topic.class, session);
			topic.addConcept(concept);
			concept.setTopic(topic);
			commonDAO.setEntityDefaultProperties(concept);
			transaction.commit();
		} finally {
			session.close();
		}
		concept.setTopic(null);
		return concept;
	}

	Topic addUpdates(Topic oldObj, Topic newObj) {
		if (newObj.getState() != null) {
			oldObj.setState(newObj.getState());
		}
		if (newObj.getName() != null) {
			oldObj.setName(newObj.getName());
		}
		oldObj.setLastUpdatedBy(newObj.getLastUpdatedBy());
		return oldObj;
	}
}
