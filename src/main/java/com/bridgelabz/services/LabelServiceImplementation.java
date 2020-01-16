package com.bridgelabz.services;

import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bridgelabz.exception.UserException;
import com.bridgelabz.model.Label;
import com.bridgelabz.model.LabelDto;
import com.bridgelabz.model.LabelUpdate;
import com.bridgelabz.model.Note;
import com.bridgelabz.model.User;
import com.bridgelabz.repository.LabelRepositoryImplementation;
import com.bridgelabz.repository.NoteRepositoryImplementation;
import com.bridgelabz.repository.UserRepositoryImplementation;
import com.bridgelabz.util.JwtGenerator;

@Service
public class LabelServiceImplementation implements ILabelService {
	@Autowired
	private LabelRepositoryImplementation labelRepositoryImplementation;

	private Label labelInformation = new Label();
	@Autowired
	private UserRepositoryImplementation userRepository;
	@Autowired
	private JwtGenerator tokenGenerator;
	@Autowired
	private NoteRepositoryImplementation noteRepositoryImplementation;

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
			Label labelInfo = labelRepositoryImplementation.fetchLabel(user.getUserId(), label.getName());
			System.out.println("Inside Label ");
			if (labelInfo == null) {
				Label labell = new Label();

				// labelInformation = modelMapper.map(label, Label.class);
				BeanUtils.copyProperties(label, labell);
				labelInformation.getLabelId();
				labelInformation.getName();
				labelInformation.setUserId(user.getUserId());
				labelRepositoryImplementation.saveLabel(labelInformation);
			} else {
				throw new UserException("label with the given name is already present");
			}
		} else {
			throw new UserException("Note does not exist with the given id");
		}
		return true;
	}

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
			Label labelInfo = labelRepositoryImplementation.fetchLabel(user.getUserId(), label.getName());
			if (labelInfo == null) {
				BeanUtils.copyProperties(label, Label.class);
				labelInformation.setUserId(user.getUserId());
				labelRepositoryImplementation.saveLabel(labelInformation);
				Note note = noteRepositoryImplementation.findById(noteId);
				note.getList().add(labelInformation);
				noteRepositoryImplementation.saveNote(note);

			} else {
				throw new UserException("label with the given name is already present");
			}
		} else {
			throw new UserException("Note does not exist with the given id");
		}
		return true;
	}

	@Override
	public boolean addLabel(Long labelId, Long noteId, String token) {
		Note note = noteRepositoryImplementation.findById(noteId);
		Label label = labelRepositoryImplementation.fetchLabelById(labelId);
		label.getList().add(note);
		labelRepositoryImplementation.saveLabel(label);
		return true;
	}

	@Override
	public boolean removeLabel(Long labelId, Long noteId, String token) {
		Note note = noteRepositoryImplementation.findById(noteId);
		Label label = labelRepositoryImplementation.fetchLabelById(labelId);
		note.getList().remove(label);
		labelRepositoryImplementation.saveLabel(label);
		return true;
	}

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
			Label labelInfo = labelRepositoryImplementation.fetchLabelById(label.getLableId());
			if (labelInfo != null) {
				labelInfo.setName(label.getLabelName());
				labelRepositoryImplementation.saveLabel(labelInfo);
			} else {
				throw new UserException("label with the given id does not exist");
			}

		} else {
			throw new UserException("user does not exist");
		}
		return true;
	}

	@Override
	public boolean deleteLabel(LabelUpdate info, String token) {
		Long id = null;

		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {

			throw new UserException("User does not exist");
		}

		User user = userRepository.getUserById(id);
		if (user != null) {
			Label labelInfo = labelRepositoryImplementation.fetchLabelById(info.getLableId());
			if (labelInfo != null) {
				labelRepositoryImplementation.deleteLabel(info.getLableId());
			} else {
				throw new UserException("Note does not exist");
			}
		}
		return true;
	}

	@Override
	public List<Label> getLabel(String token) {

		Long id;
		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {

			throw new UserException("note does not exist");
		}

		List<Label> labels = labelRepositoryImplementation.getAllLabel(id);
		return labels;

	}

	@Override
	public List<Note> getAllNotes(String token, Long labelId) {
		Label label = labelRepositoryImplementation.getLabel(labelId);
		List<Note> list = label.getList();
		System.out.println("label is" + list);

		return list;
	}
}