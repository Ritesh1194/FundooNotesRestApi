package com.bridgelabz.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.exception.UserException;
import com.bridgelabz.model.Label;
import com.bridgelabz.model.LabelDto;
import com.bridgelabz.model.LabelUpdate;
import com.bridgelabz.model.Note;
import com.bridgelabz.model.User;
import com.bridgelabz.repository.ILabelRepository;
import com.bridgelabz.repository.INoteRepository;
import com.bridgelabz.repository.UserRepositoryImplementation;
import com.bridgelabz.util.JwtGenerator;

@Service
public class LabelServiceImplementation implements ILabelService {
	@Autowired
	private ILabelRepository labelRepository;

	private Label labelInformation = new Label();
	@Autowired
	private UserRepositoryImplementation userRepository;
	@Autowired
	private JwtGenerator tokenGenerator;
	@Autowired
	private INoteRepository noteRepositoryImplementation;

	@Autowired
	ModelMapper modelMapper;

	@Transactional
	@Override
	public boolean createLabel(LabelDto label, String token) {
		Long id = null;

		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {

			throw new UserException("user does not exist");
		}
		System.out.println("Inside Label1 ");
		User user = userRepository.getUserById(id);
		System.out.println(user);
		System.out.println("Inside Label2 ");
		if (user != null) {
			System.out.println("Inside Label3 ");
			Label labelInfo = labelRepository.fetchLabel(user.getUserId(), label.getName());
			System.out.println("Inside Label ");
			if (labelInfo == null) {
				Label labell = new Label();

				labelInformation = modelMapper.map(label, Label.class);
				// BeanUtils.copyProperties(label, labell);
//				labelInformation.getLabelId();
//				labelInformation.getName();
				labelInformation.setUserId(user.getUserId());
				// labelRepository.saveLabel(labell);
				labelRepository.saveLabel(labelInformation);
			} else {
				throw new UserException("label with the given name is already present");
			}
		} else {
			throw new UserException("Note does not exist with the given id");
		}
		return true;
	}

	@Transactional
	@Override
	public boolean createLabelAndMap(LabelDto label, String token, Long noteId) {
		Long id = null;

		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {

			throw new UserException("user does not exist");
		}

		User user = userRepository.getUserById(id);
		if (user != null) {
			Label labelInfo = labelRepository.fetchLabel(user.getUserId(), label.getName());
			if (labelInfo == null) {
				BeanUtils.copyProperties(label, Label.class);
				labelInformation.setUserId(user.getUserId());
				labelRepository.saveLabel(labelInformation);
				Note note = noteRepositoryImplementation.findById(noteId);
				note.getList().add(labelInformation);
				noteRepositoryImplementation.createNote(note);

			} else {
				throw new UserException("label with the given name is already present");
			}
		} else {
			throw new UserException("Note does not exist with the given id");
		}
		return true;
	}

	@Transactional
	@Override
	public boolean addLabel(Long labelId, Long noteId, String token) {
		Note note = noteRepositoryImplementation.findById(noteId);
		System.out.println("add Label1");
		Label label = labelRepository.fetchLabelById(labelId);
		label.getList().add(note);
		System.out.println("add Label 2");
		labelRepository.saveLabel(label);
		System.out.println();
		return true;
	}

	@Transactional
	@Override
	public boolean removeLabel(Long labelId, Long noteId, String token) {
		Note note = noteRepositoryImplementation.findById(noteId);
		Label label = labelRepository.fetchLabelById(labelId);
		note.getList().remove(label);
		labelRepository.saveLabel(label);
		return true;
	}

	@Transactional
	@Override
	public boolean editLabel(LabelUpdate label, String token) {
		Long id = null;

		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {

			throw new UserException("user is not present ");
		}

		User user = userRepository.getUserById(id);
		if (user != null) {
			Label labelInfo = labelRepository.fetchLabelById(label.getLableId());
			if (labelInfo != null) {
				labelInfo.setName(label.getLabelName());
				labelRepository.saveLabel(labelInfo);
			} else {
				throw new UserException("label with the given id does not exist");
			}

		} else {
			throw new UserException("user does not exist");
		}
		return true;
	}

//	@Override
//	public boolean deleteLabel(LabelUpdate info, String token) {
//		Long id = null;
//
//		try {
//			id = (long) tokenGenerator.parseJWT(token);
//		} catch (Exception e) {
//
//			throw new UserException("User does not exist");
//		}
//
//		User user = userRepository.getUserById(id);
//		if (user != null) {
//			Label labelInfo = labelRepository.fetchLabelById(info.getLableId());
//			if (labelInfo != null) {
//				labelRepository.deleteLabel(info.getLableId());
//			} else {
//				throw new UserException("Note does not exist");
//			}
//		}
//		return true;
//	}
	@Transactional
	@Override
	public List<Label> getLabel(String token) {

		Long id;
		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {

			throw new UserException("note does not exist");
		}

		List<Label> labels = labelRepository.getAllLabel(id);
		return labels;

	}

	@Transactional
	@Override
	public List<Note> getAllNotes(String token, Long labelId) {
		Label label = labelRepository.getLabel(labelId);
		List<Note> list = label.getList();
		System.out.println("label is" + list);
		return list;
	}
}